import webapp2
import json
from google.appengine.ext import ndb
from google.appengine.api.logservice import logservice
from Users import User
import logging


class UserIdHandler(webapp2.RequestHandler):
    def head(self):
        self.response.status = 200
        self.response.headerlist = [("Content-type", "text/html")]

    # Mobile app sends post to check if id exist
    def post(self):
        id = self.request.get("UUID")
        user = (User.get_by_id(int(id)))
        if user == None:
            noUser = {}
            noUser['id'] = 0
            self.response.write(json.dumps(noUser))
            self.response.status = 200
            return

        id = user.key.id()
        userID = {}
        userID['id'] = id
        self.response.write(json.dumps(userID))
        self.response.status = 200



    # Return data on user with id
    def get(self):
        id = self.request.get("UUID")
        user = (User.get_by_id(int(id)))
        if user == None:
            userInfo = {}
            userInfo['validID'] = "false"
            self.response.write(json.dumps(userInfo))
            self.response.status = 200
            return
            #do stuff

        userInfo = {}
        userInfo['validID'] = "true"
        userInfo['firstName'] = user.firstName
        userInfo['lastName'] = user.lastName
        userInfo['phoneNumber'] = user.phoneNumber
        userInfo['currentLocation_lat'] = user.currentLocation_lat
        userInfo['currentLocation_lng'] = user.currentLocation_lng
        userInfo['deviceType'] = user.deviceType
        userInfo['deviceToken'] = user.deviceToken
        userInfo['purdueCASServiceTicket'] = user.purdueCASServiceTicket
        userInfo['isAdminUser'] = user.isAdminUser

        self.response.write(json.dumps(userInfo))
        self.response.status = 200
