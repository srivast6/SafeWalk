import webapp2
from google.appengine.ext import ndb

class User(ndb.Model):
    name = ndb.StringProperty()
    phoneNumber = ndb.StringProperty()
    currentLocation_lat = ndb.StringProperty()
    currentLocation_lng = ndb.StringProperty()
    deviceType = ndb.StringProperty() # 'iOS' or 'Android'
    deviceToken = ndb.StringProperty() # 32 bytes of data on iOS, String on Android (GCM)
    purdueCASServiceTicket = ndb.StringProperty() # Set https://www.purdue.edu/apps/account/html/cas_presentation_20110407.pdf

class UsersHandler(webapp2.RequestHandler):
    def head(self):
        self.response.status = 200
        self.response.headerlist = [("Content-type", "text/html")]

    # Mobile app sends post to create new user
    def post(self):
        self.response.status = 200
        self.response.write("Nothing to see here yet!")

    # Returns list of all users
    def get(self):
        self.response.status = 200
        self.response.write("Nothing to see yet!")


