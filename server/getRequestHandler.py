import time
import webapp2
import logging
import json
from Request import Request
from google.appengine.api.logservice import logservice
from RequestHandler import RequestHandler


class getRequestHandler(webapp2.RequestHandler):
    def head(self):
        self.response.status = 200
        self.response.headerlist = [("Content-type", "text/html")]


    # this will be a request from the app for information, server will send json to app.     
    def get(self,id):
        """Respond to a GET request."""
        user = self.getUserByUUID(id)
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
        
        

    #respond to POST Request, which will come from Safewalk App
    def post(self):
        url = self.request.url[-36:]
        user = self.getUserByUUID(url)
        user.requestAccepted = True
        self.response.status = 200


    def getUserByUUID(self,id):
        match = Request.query(Request.requestId == id).fetch()
        logging.info("match %s" %str(match[0]))
        return match[0]




