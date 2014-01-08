#import json
class Requester(object):

    def __init__(self, jsonDict):
        self.jsonDict = jsonDict

    def printInfo(self):
        print("Username:%s\n" %(self.name))
        print("Time Of Rquest:%s\n" %(self.time))
        print("phoneNumber:%s\n" %(self.number))
        print("Uregency:%s\n" %(self.urgency))
        print("Lat:%s\n" %(self.lat))
        print("Long:%s\n" %(self.long))



    def toJSON(self):
        return self.jsonDict
        #return json.dumps(self, default=lambda o: o.__dict__)


        

