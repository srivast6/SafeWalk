import datetime
import time
import webapp2
import logging
import json
import getRequestHandler
from Request import Request
from google.appengine.api.logservice import logservice
from google.appengine.ext import ndb

class RequestHandler(webapp2.RequestHandler):
    def head(self):
        self.response.status = 200
        self.response.headerlist = [("Content-type", "text/html")]


    # this will be a request from the app for information, server will send json to app.     
    def get(self):
        logging.info("Getting all open requests")
        self.response.status = 200
        self.response.headerlist = [("Content-type", "application/json")]
        requests = []
        for req in Request.getAllOpenRequests():
            requests.append(req.to_dict())
            # requests.append(encoder.default(req.to_dict()))
        self.response.write(json.dumps(requests))
        

    #respond to POST Request, which will come from Safewalk App
    def post(self):
        logging.info("Creating new request")
        #content_len = int(self.response.headers['content-length'])
        post_body = self.request.body
        print(post_body)
        #postBody is a dictionary with key
        #value pairs of json values
        json_dict = json.loads(post_body)
        r = Request(requestId = json_dict["requestId"],
                name = json_dict["name"],
                requestTime = str(datetime.datetime.now()),
                phoneNumber = json_dict["phoneNumber"],
                urgency = json_dict["urgency"],
                startLocation_lat = str(json_dict["startLocation_lat"]), 
                startLocation_lon = str(json_dict["startLocation_lon"]),
                endLocation_lat = str(json_dict["endLocation_lat"]), 
                endLocation_lon = str(json_dict["endLocation_lon"]),
                walkCompleted = False,
                requestAccepted = False)

        # Add request to datastore
        r.put()

        self.response.status = 200


