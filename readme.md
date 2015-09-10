FreeFood app SourceCode
==============

basic explaination
--------------

I created FreeFood in order for people to be able to send and receive push notifications about close food.
E.g. "back from business trip - CIMC",
"I have some rice leftovers if my room at the dorms, going out for weekend if someone wnats them"
etc...

Basically the app uses wisely two resources:
- a singleton that interacts with Parse.com server to upload and download info about the Food class (title, details, thumbnail, location ...)
- a service that talks to google location api to get the user location
In addition, there are some peculiar stuff:
- a FoodBuilder, designed to store the partial info about the food from one of the sources (GUI - title details and thumbnail, location-service) until the other is ready
- LocationSuperviser, that helps a lot communicating with the standalone service
- Thumbnail, every thumbnail can be used to store in Food (as integer) and retreive the rellevant drawable to represent it
All the rest are activities and Application classes

I reccomend viewing the temp branch as well because I used to work on it alot.

History
--------------
FreeFood was initated at the huji hackathon at April 30, 2015.
We went through it all the night, struggling with connection + design + parse + flow...
It didn't work quite well, but hi! at least we got a nice MainActivity design and some user-recognition module :)

That was that, pretty much, until I continued working on FreeFood by myself in order to submit it to POSTPC course at HUJI - The Hebrew University of Jerusalem Israel.

Right now the app supports:
- users recognition via parse
- food auto-locate by the locationCaptureService
- food upload and download via parse
- push notifications <-- (HELL YEAH! this one caused a big headache)
- ErrorActivity class to support on crashes, including guiding the users on how to handle the crisis themselves


At start, the design was with pictures of winnie the pooh. It was nice and lovely and cute and... got the app rejected from google play, so there was a lot of work to do and new pictures to find in order to upload FreeFood to google play.

I really appriciate the help of the people listed below, by sitting with me at this not-ending night at the hackathon - 
Noam Wies
Elyasaf Cohen
Orr paradise
Thanks for contributing, and initating this project!
May the bugs will run from your programs, like hobbits that just heard about some free food nearby!


Re'em Halamish