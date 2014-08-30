"""`main` is the top level module for your Flask application."""

# Import the Flask Framework
from flask import Flask
from flask import request
from flask import jsonify
from google.appengine.api.logservice import logservice
from google.appengine.ext import ndb
import logging
import datetime
from Models.Request import Requester
app = Flask(__name__)
# Note: We don't need to call run() since our application is embedded within
# the App Engine WSGI application server.


@app.route('/')
def hello():
    """Return a friendly HTTP greeting."""
    return 'Hello World!'

@app.route('/request', methods=['GET','POST'])
def new_request():
    if request.method == "POST":
        json_dict = request.get_json()
        logging.info(request.get_json())
        r = Requester(requestId = json_dict["requestId"],
        name = json_dict["name"],
        requestTime = str(datetime.datetime.now()),
        phoneNumber = json_dict["phoneNumber"],
         urgency = json_dict["urgency"],
        startLocation_lat = str(json_dict["start_lat"]), 
        startLocation_lon = str(json_dict["start_long"]),
        endLocation_lat = str(json_dict["end_lat"]), 
        endLocation_lon = str(json_dict["end_long"]),
        walkCompleted = False,
        requestAccepted = False)
        #r = Request(request.get_json())
        r.put()
        return '200'

    elif request.method =="GET":
        openRequests = Requester.getAllOpenRequests()
        logging.info(openRequests)
        reqJson = []
        for req in openRequests:
            reqJson.append(req.to_json())
        return jsonify(results = reqJson)

@app.route('/request/<int:id>', methods=['GET','POST'])
def acccept_request(id):
    if request.method == "POST":
        requestId = id
        #requestId = str(id.encode('ascii'))        
        logging.info(requestId)
        logging.info(type(requestId))
        r = Requester.get_by_id(requestId)
        r.requestAccepted = True
        r.put()
        return '200'

        


@app.errorhandler(404)
def page_not_found(e):
    """Return a custom 404 error."""
    return 'Sorry, Nothing at this URL.', 404


@app.errorhandler(500)
def page_not_found(e):
    """Return a custom 500 error."""
    return 'Sorry, unexpected error: {}'.format(e), 500
