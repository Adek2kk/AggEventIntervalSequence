class EventIntervalSequance (idS: Integer, evIntList: List[EventsInterval]){
  var sequence: List[EventsInterval] = evIntList
  var idSequence: Integer = idS

  def printSequence(): Unit =
  {
    println("Id sequence: " + idSequence)
    for(x <- sequence) {
      for(y <- x.events){
        var propString: String = ""
        if (y.attributes != null) {
          for ((k, v) <- y.attributes) {
            propString = propString + "(" + k + " , " + v + "), "
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
           propString = propString + "(" + k + " , " + v + "), "
         }
         println("INTERVAL  |   Properties: " + propString.dropRight((2)))
       }
       else {
         println("INTERVAL  |   Properties: null")
       }
     }
    }

  }
}
