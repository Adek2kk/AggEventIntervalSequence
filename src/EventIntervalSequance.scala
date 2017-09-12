class EventIntervalSequance (idS: Integer, evIntList: List[EventsInterval]){
  var sequence: List[EventsInterval] = evIntList
  var idSequence: Integer = idS

  def printSequence(): Unit =
  {
    println("Id sequence: " + idSequence)
    for(x <- sequence) {
      for(y <- x.events){
        var propString: String = ""
        for((k,v) <- y.attributes){
          propString = propString + "(" + k + " , " + v + "), "
        }
        println("EVENT  |  Timestamp: " + y.timestamp.toString + " Properties: " + propString.dropRight((2)))
      }
     if(x.interval != null) {
       var propString: String = ""
       for ((k, v) <- x.interval.attributes) {
         propString = propString + "(" + k + " , " + v + "), "
       }
       println("INTERVAL  |   Properties: " + propString.dropRight((2)))
     }
    }

  }
}
