import scala.collection.mutable.ListBuffer
import scala.io.Source

object HelloW {


  def main(args: Array[String]) {
   /* val test = "max( prad ) AS max_prad, concat( prad_text ) AS concat_prad_text"
    val test2 = "max( pradI ) AS max_pradI,max( pradI_div divide ) AS max_pradI_div, concat( pradI ) AS concat_prad_textI"*/
    val test = "avg( prad ) AS avg_prad, min( prad ) AS min_prad, max( prad ) AS max_prad, concat( prad_text ) AS concat_prad_text"
    val test2 = "avg( pradI ) AS avg_pradI, min( pradI ) AS min_pradI, max( pradI divide ) AS max_pradI, concat( pradI_text ) AS concat_pradI_text"
    val test3 = "prad < 500,prad_text == 'Alaska'"
   //val test3 = ""
    //val test4 = "pradI < 500"
    val test4 = ""
    var testClass = new SelectParser()
    val listSel = testClass.parseEvent(test)
    val listSel2 = testClass.parseInterval(test2)
    var testClass2 = new WhereParser()
    val listWhe = testClass2.parseWhere(test3)
    val listWhe2 = testClass2.parseWhere(test4)
    /*for(y <- listSel){

      println(y.operation + " " + y.attrName + " " + y.newName)
    }
    for(y <- listSel2){

      println(y.operation + " " + y.attrName + " " + y.divide + " " + y.newName)
    }

    for(y <- listWhe){

      println(y.attributeName + " " + y.operation + " " + y.valueWhere + " " + y.numericValue)
    }
    */
    var testClass3 = new TestSequences()
    var seqeunces = testClass3.setTestSequences()

    var testClass4 = new AggregateSequences()
    var testSeq = testClass4.aggragate(seqeunces, 500,listSel,listSel2,listWhe,listWhe2)
    testSeq.printSequence()

    //println(testSeq.intervalAtrributeNameExist("avg_prad"))
    println()
    testSeq.intervalAtrributeValueList("avg_pradI").foreach(println)
    //testSeq.intervalAtrributeNameList().foreach(println)
    //testSeq.exportSequenceToFile("power","C:/Users/adamc/Desktop/")

   /* var testClass5 = new ImportSequence()
    var seqImp = testClass5.importSequence("C:/Users/adamc/Desktop/power_1.txt")
    if(seqImp != null) {
      seqImp.printSequence()
    }*/

  }



}
