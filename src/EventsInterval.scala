class EventsInterval (eventsList: List[Event], interv: Interval) extends Serializable  {
  var events: List[Event] = eventsList
  var interval: Interval = interv
}
