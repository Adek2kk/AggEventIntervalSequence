import java.io.{FileNotFoundException, IOException}

import scala.collection.mutable.ListBuffer
import scala.io.Source

class ImportSequence {

  def importSequence(filePath: String): EventIntervalSequence = {
    var seqInnerList = new ListBuffer[EventsInterval]()
    var tempEventList = new ListBuffer[Event]()
    try {
      val bufferedSource  = Source.fromFile(filePath)
      val src = bufferedSource.getLines()
      val idSeqString = src.take(1).next
      val idSeq = idSeqString.toInt

      for(line <- src){
        if(line.charAt(0) == 'E'){
          if(line.charAt(line.length()-1) != '|'){
            var firstSplit = line.drop(2).split('|')
            var timestamp = firstSplit(0).toInt
            var tempMap = scala.collection.mutable.Map[String, String]()
            for(attributes <- firstSplit(1).split(';')) {
              var attriAfterSplit = attributes.split(':')
              //println(attriAfterSplit(0) + " " + attriAfterSplit(1))
              tempMap += (attriAfterSplit(0) -> attriAfterSplit(1))
            }
            tempEventList += new Event(idSeq,timestamp,tempMap)
          }
          else{
            var firstSplit = line.drop(2).split('|')
            var timestamp = firstSplit(0).toInt
            tempEventList += new Event(idSeq,timestamp,null)
          }

        }
        else if(line.charAt(0) == 'I') {
          if(line.charAt(line.length()-1) != '|') {
            var firstSplit = line.drop(2)
            var tempMap = scala.collection.mutable.Map[String, String]()
            for (attributes <- firstSplit.split(';')) {
              var attriAfterSplit = attributes.split(':')
              tempMap += (attriAfterSplit(0) -> attriAfterSplit(1))
            }
            var evInt = new EventsInterval(tempEventList.toList, new Interval(idSeq, tempMap))
            tempEventList = new ListBuffer[Event]()
            seqInnerList += evInt
          }
          else{
            var evInt = new EventsInterval(tempEventList.toList, new Interval(idSeq, null))
            tempEventList = new ListBuffer[Event]()
            seqInnerList += evInt
          }
        }

      }
      var evInt = new EventsInterval(tempEventList.toList, null)
      tempEventList = new ListBuffer[Event]()
      seqInnerList += evInt

      bufferedSource.close()
      return new EventIntervalSequence(idSeq, seqInnerList.toList)
    }
    catch {
      case e: FileNotFoundException => {println("Couldn't find that file: " + filePath);null}
      case e: IOException => {println("Got an IOException!");null}
    }




  }
}
