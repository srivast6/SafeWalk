import json

from google.appengine.ext import ndb

class Requester(ndb.Model):
    UUID = ndb.StringProperty()
    name = ndb.StringProperty()
    requestTime = ndb.DateTimeProperty()
    phoneNumber = ndb.PhoneNumberProperty()
    urgency = ndb.StringProperty()
    startLocation = ndb.GeoPtProperty()
    endLocation = ndb.GeoPtProperty()

    def __init__(self, jsonDict):
        self.jsonDict = jsonDict
        UUID = self.jsonDict["UUID"] or "00000000-0000-0000-0000-000000000000"
        name = self.jsonDict["name"] or "No-name"
        requestTime = datetime.datetime.now()
        phoneNumber = self.jsonDict["phoneNumber"] or "000-000-0000"
        urgency = self.jsonDict["urgency"] or "not urgent"
        startLocation = self.jsonDict["startLocation"] or ndb.GeoPt(52.37, 4.88)
        endLocation = self.jsonDict["endLocation"] or ndb.GeoPt("52.37", "4.88")

    def printInfo(self):
        print("Username:%s\n" %(self.name))
        print("Time Of Rquest:%s\n" %(self.time))
        print("phoneNumber:%s\n" %(self.number))
        print("Uregency:%s\n" %(self.urgency))
        print("startLocation:%s\n" %(self.startLocation))
        print("endLocation:%s\n" %(self.endLocation))

    def getUUID(self):
        return self.jsonDict["UUID"]

    def getName(self):
        return self.jsonDict["name"]

    def getRequestTime(self):
        return self.jsonDict["requestTime"]

    def getPhoneNumber(self):
        return self.jsonDict["phoneNumber"]

    def getUrgency(self):
        return self.jsonDict["urgency"]

    def toJSON(self):
        return json.dumps(self, default=lambda o: o.__dict__)

