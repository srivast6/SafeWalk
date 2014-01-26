import time
import webapp2
import logging
import json
import getRequesterHandler
from RequestHandler import RequestHandler
from Requester import Requester
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
        self.response.status = 200
        self.response.headerlist = [("Content-type", "text/html")]
        response = """ <html>
                        <header><title>SafeWalkS</title></header>
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
    webapp2.Route('/request',handler=RequestHandler, name='request'),
    webapp2.Route('/request/<:[0-9a-f]{8}[-][0-9a-f]{4}[-][0-9a-f]{4}[-][0-9a-f]{4}[-][0-9a-f]{12}>/accept',handler=getRequesterHandler.getRequesterHandler,name='getRequesterHandler')
], debug=True)
