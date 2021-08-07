# Unicast Subject

A Subject that queues up events until a single Observer subscribes to it, replays those events to it until the Observer catches up and then switches to relaying events live to this single Observer until this UnicastSubject terminates or the Observer disposes.

A UnicastSubject only allows one Observer during its lifetime. For other observers, it will throw `IllegalStateException: Only a single observer allowed.`.

## Reference

http://reactivex.io/RxJava/3.x/javadoc/io/reactivex/rxjava3/subjects/UnicastSubject.html
