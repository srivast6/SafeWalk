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
        #logging.info("keyType %" %type(User.key))
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
        self.response.status = 200
