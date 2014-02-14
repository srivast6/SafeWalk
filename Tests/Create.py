import argparse,sys,uuid,requests,datetime,json



def main(argv):
	server = "http://optical-sight-386.appspot.com"
	num = 0
	parser = argparse.ArgumentParser()
	parser.add_argument("-d",help="Turn on Debugging for Localhost",action="store_true")
	parser.add_argument("-createReq", action="store_true")
	parser.add_argument("-num",help="Number of Actions",type=int)
	args = parser.parse_args()

	num = args.num

	if(args.d):
		server = "http://localhost:8080"


	if(args.createReq):
		id = uuid.uuid4()
		name = "Mike_Doe"
		reqTime = "time"
		phoneNum = "2199233208"
		urgen = "Not"
		startLocLat = "0.0"
		startLocLon = "0.0"
		endLocLat = "0.0"
		endLocLon = "0.0"
		walkedCom = False
		reqAccepted = False

		print(server)
		for i in range(num):
			payload = {"requestId":str(id),
			           "name":name,
					   "requestTime":str(reqTime),
					   "phoneNumber":phoneNum,
					   "urgency":urgen,
					   "startLocation_lat":startLocLat,
					   "startLocation_lon":startLocLon,
					   "endLocation_lat":endLocLat,
					   "endLocation_lon":endLocLon}
			print(payload)
			r = requests.post(server+"/request",data=json.dumps(payload))
			print(r)




















if __name__ == "__main__":
	print("SafeWalk Tests")
	main(sys.argv)