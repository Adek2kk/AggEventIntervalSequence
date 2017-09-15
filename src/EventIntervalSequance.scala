import java.io._
import scala.collection.mutable.ListBuffer

@SerialVersionUID(100L)
class EventIntervalSequance (idS: Integer, evIntList: List[EventsInterval]) extends Serializable{
  var sequence: List[EventsInterval] = evIntList
  var idSequence: Integer = idS

  def printSequence(): Unit =  {
    println("Id sequence: " + idSequence)
    for(x <- sequence) {
      for(y <- x.events){
        var propString: String = ""
        if (y.attributes != null) {
          for ((k, v) <- y.attributes) {
            propString = propString + "{" + k + ": " + v + "}, "
          }
          println("EVENT  |  Timestamp: " + y.timestamp.toString + " Properties: " + propString.dropRight((2)))
        }
        else {
          println("EVENT  |  Timestamp: " + y.timestamp.toString + " Properties: null")
        }
      }
     if(x.interval != null) {
       var propString: String = ""
       if (x.interval.attributes != null) {
         for ((k, v) <- x.interval.attributes) {
           propString = propString + "{" + k + ": " + v + "}, "
         }
         println("INTERVAL  |   Properties: " + propString.dropRight((2)))
       }
       else {
         println("INTERVAL  |   Properties: null")
       }
     }
    }

  }

  def exportSequenceToFile(fileName: String, filePath: String): Unit ={
    val file = new File(filePath + fileName + "_" + idSequence + ".txt")
    val bw = new BufferedWriter(new FileWriter(file))
    bw.write(idSequence.toString + String.format("%n") )
    for(x <- sequence) {
      for(y <- x.events){
        var propString: String = ""
        if (y.attributes != null) {
          for ((k, v) <- y.attributes) {
            propString = propString + k + ":" + v + ";"
          }
          bw.write("E " + y.timestamp.toString + "|" + propString.dropRight((1)) + String.format("%n"))
        }
        else {
          bw.write("E " + y.timestamp.toString + "|" + String.format("%n"))
        }
      }
      if(x.interval != null) {
        var propString: String = ""
        if (x.interval.attributes != null) {
          for ((k, v) <- x.interval.attributes) {
            propString = propString + k + ":" + v + ";"
          }
          bw.write("I |" + propString.dropRight((1)) + String.format("%n"))
        }
        else {
          bw.write("I |" + String.format("%n"))
        }
      }
    }
    bw.close()

  }

  def eventAtrributeNameList(): List[String] =  {
    var tempList = new ListBuffer[String]()
    for(x <- sequence) {
      for (y <- x.events) {
        if (y.attributes != null) {
          for ((k, v) <- y.attributes) {
            if (!tempList.contains(k)) {
              tempList += k
            }
          }
        }
      }
    }
    return tempList.toList
  }

  def intervalAtrributeNameList(): List[String] =  {
    var tempList = new ListBuffer[String]()
    for(x <- sequence) {
      if(x.interval != null) {
        var propString: String = ""
        if (x.interval.attributes != null) {
          for ((k, v) <- x.interval.attributes) {
            if (!tempList.contains(k)) {
              tempList += k
            }
          }
        }
      }
    }
    return tempList.toList
  }

  def eventAtrributeNameExist(attName: String): Boolean =  {
    var existName = false
    for(x <- sequence) {
      for (y <- x.events) {
        if (y.attributes != null) {
          for ((k, v) <- y.attributes) {
            if (k == attName) {
              existName = true
            }
          }
        }
      }
    }
    return existName
  }

  def intervalAtrributeNameExist(attName: String): Boolean =  {
    var existName = false
    for(x <- sequence) {
      if(x.interval != null) {
        var propString: String = ""
        if (x.interval.attributes != null) {
          for ((k, v) <- x.interval.attributes) {
            if (k == attName) {
              existName = true
            }
          }
        }
      }
    }
    return existName
  }

  def eventAtrributeValueList(attName: String): List[String] =  {
    var tempList = new ListBuffer[String]()
    for(x <- sequence) {
      for (y <- x.events) {
        if (y.attributes != null) {
          for ((k, v) <- y.attributes) {
            if (k == attName) {
              if (!tempList.contains(v)) {
                tempList += v
              }
            }
          }
        }
      }
    }
    return tempList.toList
  }

  def intervalAtrributeValueList(attName: String): List[String] =  {
    var tempList = new ListBuffer[String]()
    for(x <- sequence) {
      if(x.interval != null) {
        var propString: String = ""
        if (x.interval.attributes != null) {
          for ((k, v) <- x.interval.attributes) {
            if (k == attName) {
              if (!tempList.contains(v)) {
                tempList += v
              }
            }
          }
        }
      }
    }
    return tempList.toList
  }
}
