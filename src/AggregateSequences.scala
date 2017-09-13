import org.apache.zookeeper.Watcher.Event

import scala.collection.mutable.ListBuffer

class AggregateSequences {

  var tempEvents = new ListBuffer[Event]()
  var tempIntervals = new ListBuffer[IntervalHelper]()


  def aggragate(sequences: List[EventIntervalSequance], selEvent: List[SelectEvent], selInterval: List[SelectInterval], whEvent: List[WhereEvInt], whInterval: List[WhereEvInt]): EventIntervalSequance = {

    fillTempLists(sequences,whEvent,whInterval)
    printTempListsPhaseOne()

    return null
  }

  def fillTempLists(sequences: List[EventIntervalSequance], whEvent: List[WhereEvInt], whInterval: List[WhereEvInt]) = {

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
            conditionTest = checkConditionEvent(event, whEvent)

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
            conditionTest = checkConditionInterval(pairEvInt.interval, whInterval)

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

  def checkConditionNumeric(attrVal: Double, operation: String, condVal: Double): Boolean = {

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

  def checkConditionEvent(event: Event, whEvent: List[WhereEvInt]): Boolean = {
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

  def checkConditionInterval(interval: Interval, whInterval: List[WhereEvInt]): Boolean = {
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

  def printTempListsPhaseOne()={
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
