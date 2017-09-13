import scala.collection.mutable.ListBuffer

class ImportSequence {

  def importSequence(filePath: String): EventIntervalSequance = {

    val idSeq = 50
    var testList = new ListBuffer[String]
    testList += "E 888| prad:222;prad_text:Alabama"
    testList += "E 888| prad:225;prad_text:Alabama_2"
    testList += "I pradI:226;pradI_div:944;pradI_text:OFF"
    testList += "E 888| prad:25;prad_text:Alabama2"
    testList += "I pradI:26;pradI_div:54;pradI_text:ON"
    testList += "E 888| prad:85;prad_text:Alabama3"
    testList += "I pradI:286;pradI_div:344;pradI_text:OFF"
    testList += "E 888| prad:28;prad_text:Alabama4"
    testList += "I pradI:22;pradI_div:44;pradI_text:ON"
    testList += "E 888| prad:278;prad_text:Alabama5"
    var seqInnerList = new ListBuffer[EventsInterval]()
    var tempEventList = new ListBuffer[Event]()

    for(line <- testList){
      if(line.charAt(0) == 'E'){
        var firstSplit = line.drop(2).split('|')
        var timestamp = firstSplit(0).toInt
        var tempMap = scala.collection.mutable.Map[String, String]()
        for(attributes <- firstSplit(1).split(';')) {
          var attriAfterSplit = attributes.split(':')
          tempMap += (attriAfterSplit(0) -> attriAfterSplit(1))
        }
        tempEventList += new Event(idSeq,timestamp,tempMap)
      }
      else if(line.charAt(0) == 'I') {
        var firstSplit = line.drop(2)
        var tempMap = scala.collection.mutable.Map[String, String]()
        for(attributes <- firstSplit.split(';')) {
          var attriAfterSplit = attributes.split(':')
          tempMap += (attriAfterSplit(0) -> attriAfterSplit(1))
        }
        var evInt = new EventsInterval(tempEventList.toList, new Interval(idSeq,tempMap))
        tempEventList = new ListBuffer[Event]()
        seqInnerList += evInt
      }

    }
    var evInt = new EventsInterval(tempEventList.toList, null)
    tempEventList = new ListBuffer[Event]()
    seqInnerList += evInt


  return new EventIntervalSequance(idSeq, seqInnerList.toList)
  }
}
