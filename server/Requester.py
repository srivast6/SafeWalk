import datetime
import simplejson
import time

from google.appengine.ext import ndb
from google.appengine.ext.ndb import model, query

class Requester(ndb.Model):
    requestId = ndb.StringProperty()
    name = ndb.StringProperty()
    requestTime = ndb.StringProperty()
    phoneNumber = ndb.StringProperty()
    urgency = ndb.StringProperty()
    startLocation_lat = ndb.StringProperty()
    startLocation_lon = ndb.StringProperty()
    endLocation_lat = ndb.StringProperty()
    endLocation_lon = ndb.StringProperty()
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


    @staticmethod
    def getAllOpenRequests():
        return Requester.query(Requester.walkCompleted == False).fetch()
