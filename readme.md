FreeFood app sourcecode
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

Re'em