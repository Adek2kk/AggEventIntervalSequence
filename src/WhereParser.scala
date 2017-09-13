import scala.collection.mutable.ListBuffer

class WhereParser {

  def parseWhere(where: String): List[WhereEvInt] ={
    if(where == "") {
      return null
    }
    else {
      var whereList = new ListBuffer[WhereEvInt]()
      var firstSplitArray = where.split(",")
      var tmp: String = ""
      for (x <- firstSplitArray) {
        if (x.charAt(0) == ' ') {
          tmp = x.drop(1)
        }
        else {
          tmp = x
        }

        var secondSplitArray = tmp.split(" ")
        if (secondSplitArray(2).charAt(0) == '\'') {
          var wheObj = new WhereEvInt(secondSplitArray(0), secondSplitArray(1), secondSplitArray(2).dropRight(1).drop(1), false)
          whereList += wheObj
        }
        else {
          var wheObj = new WhereEvInt(secondSplitArray(0), secondSplitArray(1), secondSplitArray(2), true)
          whereList += wheObj
        }
      }
      val finalList = whereList.toList
      return finalList
    }
  }
}
