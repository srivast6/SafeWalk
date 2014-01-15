import time
import webapp2
import logging
import json
from Requester import Requester
from google.appengine.api.logservice import logservice


HOST_NAME = '0.0.0.0' # !!!REMEMBER TO CHANGE THIS!!!
PORT_NUMBER = 8080 # Maybe set this to 9000.

openRequests = []

#need to add a setting temp for right now in app for ip of server. Right now it is hard coded


class MyHandler(webapp2.RequestHandler):
    def head(self):
        self.response.status = 200
        self.response.headerlist = [("Content-type", "text/html")]


    # this will be a request from the app for information, server will send json to app.     
    def get(self):
        """Respond to a GET request."""
        self.response.status = 200
        self.response.headerlist = [("Content-type", "application/json")]
        requests = []
        for req in openRequests:
            requests.append(req.toJSON())
        self.response.write(json.dumps(requests))
        

    #respond to POST Request, which will come from Safewalk App
    def post(self):
        logging.info("Got a post!")
        content_len = int(self.response.headers['content-length'])
        post_body = self.request.body
        #postBody is a dictionary with key-value pairs of json values
        marked = json.loads(post_body)
        r = Requester(marked)
        openRequests.append(r)
        self.response.status = 200



application = webapp2.WSGIApplication([
    ('/', MyHandler),
], debug=True)
