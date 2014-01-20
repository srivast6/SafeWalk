import datetime

from google.appengine.ext import ndb

from flask.json import JSONEncoder

class Requester(ndb.Model):
    requestId = ndb.StringProperty()
    name = ndb.StringProperty()
    requestTime = ndb.DateTimeProperty()
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

    @classmethod
    def toJSON(self):
        encoder = JSONEncoder()
        d = self.to_dict()
        return encoder.default(d)
        # return json.dumps({'requestId': str(self.requestId),
        #     'name': str(self.name),
        #     'requestTime': str(self.requestTime),
        #     'phoneNumber': str(self.phoneNumber),
        #     'urgency': str(self.urgency),
        #     'startLocation_lat': str(self.startLocation_lat),
        #     'startLocation_lon': str(self.startLocation_lon),
        #     'endLocation_lat': str(self.endLocation_lat),
        #     'endLocation_lon': str(self.endLocation_lon),
        #     'walkCompleted': str(self.walkCompleted)})
  
    @staticmethod
    def getAllOpenRequests():
        return Requester.query(Requester.walkCompleted == False).fetch()
