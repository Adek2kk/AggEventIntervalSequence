import scala.collection.mutable.ListBuffer

object HelloW {


  def main(args: Array[String]) {
    val test = "max( prad )"
    val test2 = "max( pradI )"
    //val test3 = "prad < 500,prad_text == 'Alaska'"
    val test3 = ""
    val test4 = "pradI < 500"
    //val test = ""
    var testClass = new SelectParser()
    val listSel = testClass.parseEvent(test)
    val listSel2 = testClass.parseInterval(test2)
    var testClass2 = new WhereParser()
    val listWhe = testClass2.parseWhere(test3)
    val listWhe2 = testClass2.parseWhere(test4)
   /* for(y <- listSel){

      println(y.operation + " " + y.attrName)
    }
    for(y <- listSel2){

      println(y.operation + " " + y.attrName + " " + y.divide)
    }

    for(y <- listWhe){

      println(y.attributeName + " " + y.operation + " " + y.valueWhere + " " + y.numericValue)
    }
    */
    var testClass3 = new TestSequences()
    var seqeunces = testClass3.setTestSequences()

    var testClass4 = new AggregateSequences()
    testClass4.aggragate(seqeunces,listSel,listSel2,listWhe,listWhe2)

  }



}
