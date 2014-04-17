import time
import webapp2
import logging
import json
import getRequestHandler
from RequestHandler import RequestHandler
from Request import Request
from Users import UsersHandler
from UserIdHandler import UserIdHandler
from google.appengine.api.logservice import logservice

#openRequests = []

#need to add a setting temp for right now in app for ip of server. Right now it is hard coded




class HomeHandler(webapp2.RequestHandler):
    def head(self):
        self.response.status = 200
        self.response.headerlist = [("Content-type", "text/html")]


    # this will be a request from the app for information, server will send json to app.     
    def get(self):
        """Respond to a GET request."""
        logging.info("HomeHandler get a GET Request")
        self.response.status = 200
        self.response.headerlist = [("Content-type", "text/html")]
        response = """ <html>
                        <header><title>SafeWalk</title></header>
                        <body>
                                SafeWalk Index
                        </body>
                        </html>
                        """
        self.response.write(response)
        

    def post(self):
        self.response.status = 200



application = webapp2.WSGIApplication([
    webapp2.Route(r'/', handler=HomeHandler, name='home'),
    webapp2.Route(r'/request',handler=RequestHandler, name='request'),
    webapp2.Route(r'/request/<requestId:[0-9a-f]{8}[-][0-9a-f]{4}[-][0-9a-f]{4}[-][0-9a-f]{4}[-][0-9a-f]{12}>/accept',handler=getRequestHandler.getRequestHandler,name='getRequestHandler'),
    webapp2.Route(r'/users',handler=UsersHandler, name='users'),
    webapp2.Route(r'/users/',handler=UserIdHandler,name='UserIdHandler')
], debug=True)
