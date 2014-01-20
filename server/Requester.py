import json
import datetime

from google.appengine.ext import ndb

class Requester(ndb.Model):
    requestId = ndb.StringProperty()
    name = ndb.StringProperty()
    requestTime = ndb.DateTimeProperty()
    phoneNumber = ndb.StringProperty()
    urgency = ndb.StringProperty()
    startLocation = ndb.GeoPtProperty()
    endLocation = ndb.GeoPtProperty()
    walkCompleted = ndb.BooleanProperty()

    @classmethod
    def printInfo(self):
        print("Username:%s\n" %(self.name))
        print("Time Of Rquest:%s\n" %(self.time))
        print("phoneNumber:%s\n" %(self.number))
        print("Uregency:%s\n" %(self.urgency))
        print("startLocation:%s\n" %(self.startLocation))
        print("endLocation:%s\n" %(self.endLocation))
        print("walkCompleted:%s\n" %(self.walkCompleted))

    @classmethod
    def toJSON(self):
        return json.dumps(self, default=lambda o: o.__dict__)
  
    @staticmethod
    def getAllOpenRequests():
        return Requester.query(Requester.walkCompleted == False)
