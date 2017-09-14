import org.apache.zookeeper.Watcher.Event

import scala.collection.mutable.ListBuffer

class AggregateSequences {

  var tempEvents = new ListBuffer[Event]()
  var tempIntervals = new ListBuffer[IntervalHelper]()
  var selEvent: List[SelectEvent] = null
  var selInterval: List[SelectInterval] = null
  var whEvent: List[WhereEvInt] = null
  var whInterval: List[WhereEvInt] = null
  var idSeq: Integer = 0

  def aggragate(sequences: List[EventIntervalSequance], idSequence: Integer, selectEvent: List[SelectEvent], selectInterval: List[SelectInterval], whereEvent: List[WhereEvInt], whereInterval: List[WhereEvInt]): EventIntervalSequance = {

    selEvent = selectEvent
    selInterval = selectInterval
    whEvent = whereEvent
    whInterval = whereInterval
    idSeq = idSequence
    fillTempLists(sequences)
    //printTempListsPhaseOne()


    var finalSeq = createNewSequence()


    return finalSeq
  }

  private def fillTempLists(sequences: List[EventIntervalSequance]) = {

    for(sequence <- sequences) {
      var timeStart = -1
      var timeEnd = -1
      var tempInterval = new Interval(null,null)
      var conditionTest = true
      for(pairEvInt <- sequence.sequence){

        for(event <- pairEvInt.events) {
          timeEnd = event.timestamp
          //Sprawdzenie warunków where
          if (event.attributes == null || whEvent == null) {
            tempEvents += new Event(event.idSequence, event.timestamp, event.attributes)
          }
          else {
            conditionTest = checkConditionEvent(event)

            //Niespełnienie warunku, atrybuty na null
            if (conditionTest == false) {
              tempEvents += new Event(event.idSequence, event.timestamp, null)
              conditionTest = true
            }
            else {
              tempEvents += new Event(event.idSequence, event.timestamp, event.attributes)
            }
          }
          }


        //Dodanie interwału
        if( timeStart != -1) {
            tempIntervals += new IntervalHelper(tempInterval, timeStart, timeEnd)

          }
        //Ustawienie kolejnego interwalu na nastepne przejscie petli
        if( pairEvInt.interval != null) {

          if (pairEvInt.interval.attributes == null || whInterval == null) {
            tempInterval = new Interval(pairEvInt.interval.idSequence, pairEvInt.interval.attributes)
          }
          else {
            conditionTest = checkConditionInterval(pairEvInt.interval)

            //Niespełnienie warunku, atrybuty na null
            if (conditionTest == false) {
              tempInterval = new Interval(pairEvInt.interval.idSequence, null)
              conditionTest = true
            }
            else {
              tempInterval = new Interval(pairEvInt.interval.idSequence, pairEvInt.interval.attributes)
            }
          }



        }
        //Czast koncowy staje sie nowym czasem poczatkowym
        timeStart = timeEnd
      }
    }
  }

  private def createNewSequence(): EventIntervalSequance = {
    var timeStart = tempEvents.toList.sortWith(_.timestamp < _.timestamp).head.timestamp
    var timeEnd = 0

   /* var eventCount = 0
    var eventNullCount = 0

    var interCount = 0
    var interNullCount = 0*/

    var sameTimeEventList = new ListBuffer[Event]()
    var innerSeqList = new ListBuffer[EventsInterval]()

    for(ev <- tempEvents.sortWith(_.timestamp < _.timestamp)) {
      if(ev.timestamp == timeStart) {

          if(ev.attributes != null){
            sameTimeEventList += new Event(ev.idSequence,ev.timestamp,ev.attributes)
            //Na podstawie select obliczenie wartości
            calculateEventSelect(ev)
          }
      }
      else {
        timeEnd = ev.timestamp
        //println("Obecny czas: " + timeStart + " | " + timeEnd)
        for(interv <- tempIntervals.filter(x => (x.end > timeStart && x.start < timeEnd))){
          //println(interv.start + " " + interv.end )
          if(interv.interval.attributes != null){
            //Select dla interval
            calculateIntervalSelect(interv, timeStart, timeEnd)

          }
        }

        if(sameTimeEventList.length == 0){
          sameTimeEventList += new Event(idSeq,timeStart,null)
        }
        else{
          for(ev <- sameTimeEventList){
            ev.attributes = addAttrEvent(ev.attributes)
          }

        }
        //zakonczenie listy eventow
        //tworzenie nowego interwalu
        var tempAttributeIntervalMap = createAttrInterval()
        if(tempAttributeIntervalMap.size == 0){
          var tempInterval = new Interval(idSeq,null)
          var pairEvInt = new EventsInterval(sameTimeEventList.toList,tempInterval)
          innerSeqList += pairEvInt
        }
        else{
          var tempInterval = new Interval(idSeq,tempAttributeIntervalMap)
          var pairEvInt = new EventsInterval(sameTimeEventList.toList,tempInterval)
          innerSeqList += pairEvInt
        }




        //Porządki
        sameTimeEventList.clear()

        //Wyczyszczenie wartości dla selEvi selInt
        resetSelectLists()

        timeStart = ev.timestamp
        if(ev.attributes != null) {
          sameTimeEventList += new Event(ev.idSequence, ev.timestamp, ev.attributes)
          //Na podstawie select obliczenie wartości
          calculateEventSelect(ev)
        }

      }
    }

    if(sameTimeEventList.length == 0){
      sameTimeEventList += new Event(idSeq,timeStart,null)
    }
    else{
      //Pętla dodająca agregacja
      for(ev <- sameTimeEventList){
        ev.attributes = addAttrEvent(ev.attributes)
      }
    }
    var pairEvInt = new EventsInterval(sameTimeEventList.toList,null)
    innerSeqList += pairEvInt
    var finalSeq = new EventIntervalSequance(idSeq,innerSeqList.toList)
    return finalSeq
  }


  private def checkConditionNumeric(attrVal: Double, operation: String, condVal: Double): Boolean = {

    val result = operation match {
      case "<" => attrVal < condVal
      case "<=" => attrVal <= condVal
      case ">" => attrVal > condVal
      case ">=" => attrVal >= condVal
      case "!=" => attrVal != condVal
      case "==" => attrVal == condVal
      case  _ => false
    }

    return result
  }

  private def checkConditionEvent(event: Event): Boolean = {
    var conditionTest = true
    for (condition <- whEvent) {
      if (conditionTest == true && event.attributes.getOrElse(condition.attributeName, null) != null) {
        if (condition.numericValue == true) {
          conditionTest = checkConditionNumeric(event.attributes.getOrElse(condition.attributeName, null).toDouble, condition.operation, condition.valueWhere.toDouble)
        }
        else {
          if (condition.operation == "==") {
            conditionTest = condition.valueWhere == event.attributes.getOrElse(condition.attributeName, null)
          }
          else if (condition.operation == "!=") {
            conditionTest = condition.valueWhere != event.attributes.getOrElse(condition.attributeName, null)
          }
          else {
            conditionTest = false
          }
        }

      }
      else {
        conditionTest = false
      }
    }
    return conditionTest
  }

  private def checkConditionInterval(interval: Interval): Boolean = {
    var conditionTest = true
    for (condition <- whInterval) {
      if (conditionTest == true && interval.attributes.getOrElse(condition.attributeName, null) != null) {
        if (condition.numericValue == true) {
          conditionTest = checkConditionNumeric(interval.attributes.getOrElse(condition.attributeName, null).toDouble, condition.operation, condition.valueWhere.toDouble)
        }
        else {
          if (condition.operation == "==") {
            conditionTest = condition.valueWhere == interval.attributes.getOrElse(condition.attributeName, null)
          }
          else if (condition.operation == "!=") {
            conditionTest = condition.valueWhere != interval.attributes.getOrElse(condition.attributeName, null)
          }
          else {
            conditionTest = false
          }
        }

      }
      else {
        conditionTest = false
      }
    }
    return conditionTest
  }

  private def calculateEventSelect(event: Event) = {

    for(sel <- selEvent) {
      var eventValue = event.attributes.getOrElse(sel.attrName, null)
      if( eventValue != null){
        if(sel.operation != "concat"){
          var eventValueDouble = eventValue.toDouble

          if(sel.operation == "max"){
            if(sel.firstValueFlag == true) {
              sel.firstValueFlag = false
              sel.maxValue = eventValueDouble
            }
            else{
              if(sel.maxValue < eventValueDouble) {
                sel.maxValue = eventValueDouble
              }
            }
          }

          else if(sel.operation == "min"){
            if(sel.firstValueFlag == true) {
              sel.firstValueFlag = false
              sel.minValue = eventValueDouble
            }
            else{
              if(sel.minValue > eventValueDouble) {
                sel.minValue = eventValueDouble
              }
            }
          }

          else if(sel.operation == "avg"){
            if(sel.firstValueFlag == true) {
              sel.firstValueFlag = false
              sel.avgValue = eventValueDouble
              sel.sumValue = eventValueDouble
              sel.countValue = 1
            }
            else{
              sel.sumValue += eventValueDouble
              sel.countValue += 1
              sel.avgValue = sel.sumValue/sel.countValue
            }
          }

          else if(sel.operation == "sum"){
            if(sel.firstValueFlag == true) {
              sel.firstValueFlag = false
              sel.sumValue = eventValueDouble
            }
            else{
              sel.sumValue += eventValueDouble
            }
          }

          else if(sel.operation == "count"){
            if(sel.firstValueFlag == true) {
              sel.firstValueFlag = false
              sel.countValue = 1
            }
            else{
              sel.countValue += 1
            }
          }
        }
        else {
          if(sel.firstValueFlag == true) {
            sel.firstValueFlag = false
            sel.stringValue = eventValue + ","
          }
          else{
            sel.stringValue += eventValue + ","
          }
        }

      }
    }
  }

  private def addAttrEvent(attMap: collection.mutable.Map[String, String]):collection.mutable.Map[String, String] = {

    for(sel <- selEvent) {
      if(sel.firstValueFlag == false){
        if(sel.operation != "concat") {

          sel.operation match {
            case "max" => attMap += (sel.newName -> sel.maxValue.toString())
            case "min" => attMap += (sel.newName -> sel.minValue.toString())
            case "avg" => attMap += (sel.newName -> sel.avgValue.toString())
            case "count" => attMap += (sel.newName -> sel.countValue.toString())
            case "sum" => attMap += (sel.newName -> sel.sumValue.toString())
          }
        }
        else {
          if(sel.stringValue.count(_ == ',') == 1){
            attMap += (sel.newName -> sel.stringValue.dropRight(1))
          }
          else {
            var tempString = "[" + sel.stringValue.dropRight(1) + "]"
            attMap += (sel.newName -> tempString)
          }

        }
      }
    }

    return attMap
  }

  private def calculateIntervalSelect(inter: IntervalHelper, timeStart: Integer, timeEnd: Integer) = {
    var intervalValueDouble:Double = 0
    for(sel <- selInterval) {
      var intervalValue = inter.interval.attributes.getOrElse(sel.attrName, null)
      if( intervalValue != null){
        if(sel.operation != "concat"){
          if(sel.divide == true){
            intervalValueDouble = calculateDivIntervalValue(intervalValue.toDouble,inter.start,inter.end,timeStart,timeEnd)
          }
          else {
            intervalValueDouble = intervalValue.toDouble
          }

          if(sel.operation == "max"){
            if(sel.firstValueFlag == true) {
              sel.firstValueFlag = false
              sel.maxValue = intervalValueDouble
            }
            else{
              if(sel.maxValue < intervalValueDouble) {
                sel.maxValue = intervalValueDouble
              }
            }
          }

          else if(sel.operation == "min"){
            if(sel.firstValueFlag == true) {
              sel.firstValueFlag = false
              sel.minValue = intervalValueDouble
            }
            else{
              if(sel.minValue > intervalValueDouble) {
                sel.minValue = intervalValueDouble
              }
            }
          }

          else if(sel.operation == "avg"){
            if(sel.firstValueFlag == true) {
              sel.firstValueFlag = false
              sel.avgValue = intervalValueDouble
              sel.sumValue = intervalValueDouble
              sel.countValue = 1
            }
            else{
              sel.sumValue += intervalValueDouble
              sel.countValue += 1
              sel.avgValue = sel.sumValue/sel.countValue
            }
          }

          else if(sel.operation == "sum"){
            if(sel.firstValueFlag == true) {
              sel.firstValueFlag = false
              sel.sumValue = intervalValueDouble
            }
            else{
              sel.sumValue += intervalValueDouble
            }
          }

          else if(sel.operation == "count"){
            if(sel.firstValueFlag == true) {
              sel.firstValueFlag = false
              sel.countValue = 1
            }
            else{
              sel.countValue += 1
            }
          }
        }
        else {
          if(sel.firstValueFlag == true) {
            sel.firstValueFlag = false
            sel.stringValue = intervalValue + ","
          }
          else{
            sel.stringValue += intervalValue + ","
          }
        }

      }
    }
  }

  private def createAttrInterval():collection.mutable.Map[String, String] = {

    var attMap = scala.collection.mutable.Map[String, String]()
    for(sel <- selInterval) {
      if(sel.firstValueFlag == false){
        if(sel.operation != "concat") {

          sel.operation match {
            case "max" => attMap += (sel.newName -> sel.maxValue.toString())
            case "min" => attMap += (sel.newName -> sel.minValue.toString())
            case "avg" => attMap += (sel.newName -> sel.avgValue.toString())
            case "count" => attMap += (sel.newName -> sel.countValue.toString())
            case "sum" => attMap += (sel.newName -> sel.sumValue.toString())
          }
        }
        else {
          if(sel.stringValue.count(_ == ',') == 1){
            attMap += (sel.newName -> sel.stringValue.dropRight(1))
          }
          else {
            var tempString = "[" + sel.stringValue.dropRight(1) + "]"
            attMap += (sel.newName -> tempString)
          }

        }
      }
    }

    return attMap
  }

  private def calculateDivIntervalValue(value: Double, inStart: Integer, inEnd: Integer, inStartNew: Integer, inEndNew: Integer): Double = {

    var oneUnitValue = value/(inEnd-inStart)
    var howManyUnit = 0
    if(inStart <= inStartNew){
      if(inEnd <= inEndNew){
        howManyUnit = inEnd - inStartNew
      }
      else {
        howManyUnit = inEndNew - inStartNew
      }
    }
    else {
      if(inEnd <= inEndNew){
        howManyUnit = inEnd - inStart
      }
      else {
        howManyUnit = inEndNew - inStart
      }
    }

    return oneUnitValue * howManyUnit
  }

  private def resetSelectLists() = {
    for(x <- selEvent){
      x.clear
    }
    for(x <- selInterval){
      x.clear
    }
  }

  private def printTempListsPhaseOne()={
    for(ev <- tempEvents.sortWith(_.timestamp < _.timestamp)) {
      var propString: String = ""
      if (ev.attributes != null) {
        for ((k, v) <- ev.attributes) {
          propString = propString + "(" + k + " , " + v + "), "
        }
        println("EVENT  |  Timestamp: " + ev.timestamp.toString + " Properties: " + propString.dropRight((2)))
      }
      else {
        println("EVENT  |  Timestamp: " + ev.timestamp.toString + " Properties: null")
      }
    }

    for(int <- tempIntervals.sortWith(_.start < _.start)) {
      var propString: String = ""
      if (int.interval.attributes != null) {
        for ((k, v) <- int.interval.attributes) {
          propString = propString + "(" + k + " , " + v + "), "
        }
        println("INTERVAL  |  TimestampStart: " + int.start.toString + " TimestampEnd: " + int.end.toString + " | Properties: " + propString.dropRight((2)))
      }
      else {
        println("INTERVAL  |  TimestampStart: " + int.start.toString + " TimestampEnd: " + int.end.toString + " | Properties: null")
      }
    }
  }

}
