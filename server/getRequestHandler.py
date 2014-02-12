import time
import webapp2
import logging
import json
from Request import Request
from google.appengine.api.logservice import logservice
from gcm import GCM
from RequestHandler import RequestHandler
from Users import User


class getRequestHandler(webapp2.RequestHandler):
    def head(self):
        self.response.status = 200
        self.response.headerlist = [("Content-type", "text/html")]


    # this will be a request from the app for information, server will send json to app.     
    def get(self, requestId=""):
        """Respond to a GET request."""
        user = self.getRequestByUUID(requestId)
        user.requestAccepted = True
        user.put()
        logging.info("rquestAccepted = %s" %str(user.requestAccepted))
        


        self.response.status = 200
        self.response.headerlist = [("Content-type", "text/html")]
        response = """ <html>
                        <header><title>Get a User</title></header>
                        <body>
                                User Info:
                                    %s<br>
                                    %s<br>

                        </body>
                        </html>
                        """ % ("test","test")
        self.response.write(response)
        
        # Send everyone a push notification about the request being accepted
        gcm = GCM('AIzaSyC6y8uyxPSjxPABKieRa2iB2wLxFVyJuQY')
        data = {'param1': 'value1', 'param2': 'value2', 'appName': 'SafeWalk'}
        users = User.query().fetch()
        gcm_ids = []
        for user in users:
            logging.info("user = %s" %str(user))
            logging.info("this gcm_id = %s" %str(user.gcmID))
            gcm_ids.append(user.gcmID)
        logging.info("gcm_ids = %s" %str(gcm_ids))
        response = gcm.json_request(registration_ids=gcm_ids, data=data)
        
        

    #respond to POST Request, which will come from Safewalk App
    def post(self, requestId=""):
        request = self.getRequestByUUID(requestId)
        request.requestAccepted = True
        self.response.status = 200

        # Send everyone a push notification about the request being accepted
        gcm = GCM('AIzaSyC6y8uyxPSjxPABKieRa2iB2wLxFVyJuQY')
        data = {'param1': 'value1', 'param2': 'value2', 'appName': 'SafeWalk'}
        users = User.query().fetch()
        gcm_ids = []
        for user in users:
            gcm_ids.append(user.gcmID)
        response = gcm.json_request(registration_ids=gcm_ids, data=data)


    def getRequestByUUID(self,id):
        match = Request.query(Request.requestId == id).fetch()
        logging.info("match %s" %str(match[0]))
        return match[0]




