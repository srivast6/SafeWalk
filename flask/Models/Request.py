import datetime
import time
import logging

from google.appengine.ext import ndb
from google.appengine.ext.ndb import model, query

class Request(ndb.Model):
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
    requestAccepted = ndb.BooleanProperty()


    def to_json(self):
        return {"requestId":self.requestId,
                "name":self.name,
                "requestTime":self.requestTime,
                "phoneNumber":self.phoneNumber,
                "urgency":self.urgency,
                "start_lat":self.startLocation_lat,
                "start_long":self.startLocation_lon,
                 "end_lat":self.endLocation_lat, 
                 "end_long":self.endLocation_lon,
                 "walk_completed":self.walkCompleted, 
                 "request_accepted":self.requestAccepted
                 }
        


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
        return Request.query(Request.requestAccepted == False).fetch()
