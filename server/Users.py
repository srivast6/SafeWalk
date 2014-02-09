import webapp2
import json
from google.appengine.ext import ndb
from google.appengine.api.logservice import logservice
import logging

class User(ndb.Model):
    firstName = ndb.StringProperty()
    lastName = ndb.StringProperty()
    phoneNumber = ndb.StringProperty()
    currentLocation_lat = ndb.StringProperty()
    currentLocation_lng = ndb.StringProperty()
    deviceType = ndb.StringProperty() # 'iOS' or 'Android'
    deviceToken = ndb.StringProperty() # 32 bytes of data on iOS, String on Android (GCM)
    purdueCASServiceTicket = ndb.StringProperty() # Set https://www.purdue.edu/apps/account/html/cas_presentation_20110407.pdf
    isAdminUser = ndb.BooleanProperty()


class UsersHandler(webapp2.RequestHandler):
    def head(self):
        self.response.status = 200
        self.response.headerlist = [("Content-type", "text/html")]

    # Mobile app sends post to create new user
    def post(self):
        self.response.status = 200
        newUser = User(firstName=self.request.get("firstName"),
                lastName=self.request.get("lastName"),
                phoneNumber=self.request.get("phoneNumber"),
                currentLocation_lat=self.request.get("currentLocation_lat"),
                currentLocation_lng=self.request.get("currentLocation_lng"),
                deviceType="Android", #TODO: Get this from the device
                deviceToken=self.request.get("deviceToken"),
                purdueCASServiceTicket=self.request.get("purdueCASServiceTicket"),
                isAdminUser=False)
        newUser.put()
        id = {}
        id['id'] = str(newUser.key.id())
        self.response.write(json.dumps(id))



    # Returns list of all users
    def get(self):
        self.response.status = 200
        qry = User.query()
        numToFetch = qry.count()
        results = qry.fetch(numToFetch)
        users = []
        for user in results:
            users.append(user.to_dict())
        self.response.write(json.dumps(users))


