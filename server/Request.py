import datetime
import time

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

    def __init__(self,json_dict):
        self.requestId = json_dict["requestId"]
        self.name = json_dict["name"]
        self.requestTime = str(datetime.datetime.now())
        self.phoneNumber = json_dict["phoneNumber"]
        self.urgency = json_dict["urgency"]
        self.startLocation_lat = str(json_dict["startLocation_lat"])
        self.startLocation_lon = str(json_dict["startLocation_lon"])
        self.endLocation_lat = str(json_dict["endLocation_lat"])
        self.endLocation_lon = str(json_dict["endLocation_lon"])
        self.walkCompleted = False,
        self.requestAccepted = False


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
