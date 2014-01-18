import time
import webapp2
import logging
import json
from Requester import Requester
from google.appengine.api.logservice import logservice
from RequestHandler import RequestHandler


class getRequesterHandler(webapp2.RequestHandler):
    def head(self):
        self.response.status = 200
        self.response.headerlist = [("Content-type", "text/html")]


    # this will be a request from the app for information, server will send json to app.     
    def get(self):
        """Respond to a GET request."""
        url = self.request.url[-36:]
        logging.info("url %s"%url)
        user = self.getUserByUUID(url)


        self.response.status = 200
        self.response.headerlist = [("Content-type", "text/html")]
        response = """ <html>
                        <header><title>Get a User</title></header>
                        <body>
                                User Info:
                                	
                                
                       
                                

                        </body>
                        </html>
                        """
        self.response.write(response)
        

    #respond to POST Request, which will come from Safewalk App
    def post(self):
        self.response.status = 200


    def getUserByUUID(self,url):
    	logging.info("size of req %d" %len(RequestHandler.openRequests))
    	for req in RequestHandler.openRequests:
    		logging.info("User UUID %s"%str(req.getUUID()))
    		if req.getUUID() == url:
    			return req


