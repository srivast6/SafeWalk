import time
import webapp2
import logging
import json
import getRequesterHandler
from Requester import Requester
from google.appengine.api.logservice import logservice

class RequestHandler(webapp2.RequestHandler):
    openRequests = []
    def head(self):
        self.response.status = 200
        self.response.headerlist = [("Content-type", "text/html")]


    # this will be a request from the app for information, server will send json to app.     
    def get(self):
        """Respond to a GET request."""
        userAgent = str(self.request.headers['User-Agent'])
        logging.info("userAgent = %s" %userAgent)
        self.response.status = 200
        self.response.headerlist = [("Content-type", "application/json")]
        requests = []
        if userAgent == "android-async-http/1.4.4 (http://loopj.com/android-async-http)":
            for req in RequestHandler.openRequests:
                requests.append(req.toJSON())
            self.response.write(json.dumps(requests))
        

    #respond to POST Request, which will come from Safewalk App
    def post(self):
        
        #content_len = int(self.response.headers['content-length'])
        post_body = self.request.body
        #postBody is a dictionary with key
        #value pairs of json values
        marked = json.loads(post_body)
        r = Requester(marked)

        RequestHandler.openRequests.append(r)
        self.response.status = 200


