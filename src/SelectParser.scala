import scala.collection.mutable.ListBuffer

class SelectParser {

  def parseEvent(select: String): List[SelectEvent] ={

    var selectList = new ListBuffer[SelectEvent]()
    var firstSplitArray = select.split(",")
    var tmp: String = ""
    for(x <- firstSplitArray) {
      if (x.charAt(0) == ' ') {
        tmp = x.drop(1)
      }
      else {
      tmp = x
      }
      var secondSplitArray = tmp.replace("  "," ").split(" ")
      var selEv = new SelectEvent(secondSplitArray(0).dropRight(1), secondSplitArray(1), secondSplitArray(4))
      selectList += selEv
    }
      val  finalList = selectList.toList
      return finalList
    }

  def parseInterval(select: String): List[SelectInterval] ={

    var selectList = new ListBuffer[SelectInterval]()
    var firstSplitArray = select.split(",")
    var tmp: String = ""
    for(x <- firstSplitArray) {
      if (x.charAt(0) == ' ') {
        tmp = x.drop(1)
      }
      else {
        tmp = x
      }

      var secondSplitArray = tmp.replace("  "," ").split(" ")
      if(secondSplitArray(2) == "divide") {
        var selInt = new SelectInterval(secondSplitArray(0).dropRight(1), secondSplitArray(1), true, secondSplitArray(5))
        selectList += selInt
      }
      else {
        var selInt = new SelectInterval(secondSplitArray(0).dropRight(1), secondSplitArray(1), false, secondSplitArray(4))
        selectList += selInt
      }

    }
    val  finalList = selectList.toList
    return finalList
  }
}
