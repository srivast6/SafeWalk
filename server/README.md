Safewalk Python Server
========

Runing Server in Dev Mode on Machine
========
 * dev_appserver.py --host=0.0.0.0  --port=80 ~/workspace/SafeWalk/server/
 * If you use this, be sure to change the host string on the app. 
 *

Pushing to app engine
===== 
 * appcfg.py update ~/workspace/SafeWalk/server/
 * `If you use multi-factor authentication on Google, use the --oauth2 command line option with appcfg.py`
 * This will push the server to host http://optical-sight-386.appspot.com/
