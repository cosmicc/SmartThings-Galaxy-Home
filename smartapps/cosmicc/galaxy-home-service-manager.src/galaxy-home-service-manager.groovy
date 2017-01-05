/*
 *  Galaxy Home Service Manager SmartApp
 *
*/
definition(
    name: "Galaxy Home Service Manager",
    namespace: "cosmicc",
    author: "cosmicc",
    description: "Galaxy Home Module Service Manager SmartApp",
    category: "My Apps",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    oauth: true)

// Preferences and setup pages
preferences {
    page(name: "page1", title: "Particle Login", nextPage: "page2", uninstall: true) {
        section("Username and Password") {
        	input "particleUsername", "text", title: "Your Particle.io Username", required: true
            input "particlePassword", "password", title: "Your Particle.io Password", required: true
        }
    }
}

page(name: "page2", title: "Check Auth", nextPage: "page3", install: false, uninstall: false)
page(name: "page3", title: "Pick Device", install: true, uninstall: false)

def page2() {
   	dynamicPage(name: "page2") {
   		section("Generate Token") {
			if (loginToParticle()) {
            	paragraph title: "Success logging in to Particle.io", "Login Successful"            
            }
            else {
            	paragraph title: "Error logging in to Particle.io", "Check your username and password or check the log for other errors"
            }
		}
	}
}

def page3() {
	dynamicPage(name: "page3") {
       	section("Select Particle Devices"){
   		    //def particleDevices = getDevices()
            input "alldevices", "capability.beacon", title: "All Devices", multiple: true
   	    	//input(name: "particleDevice", type: "enum", title: "Select ALL the Galaxy Home Devices", required: true, multiple: true, options: particleDevices)
       	}
	}
}

def installed() {
	state.webhookName = "motion"
    state.appURL = "https://graph-na02-useast1.api.smartthings.com/api/smartapps/installations/${app.id}/${state.webhookName}/{{PARTICLE_EVENT_VALUE}}/{{PARTICLE_DEVICE_ID}}?access_token=${state.accessToken}"
	log.debug "Galaxy Home Service Manager SmartApp Installed"
    //schedule("0 */3 * * * ?", poll_devices)
    schedule("0 * * * * ?", huechange)
    log.debug "Device Poll Schedule started"
    checkWebhook() 
    state.ghue = 0
    //setupSensors()
}

def updated() {
	log.debug "GHSM SmartApp Updated Successfully"
	unsubscribe()
    checkWebhook()
    state.ghue = 0
    //setupSensors()
}

// Log in to Particle.io
def loginToParticle() {
	   	if (!state.particleToken){
	   		def clientAuth = "particle:particle"
	   		clientAuth = "Basic " + clientAuth.encodeAsBase64().toString()
	   		def params = [
	        	headers: [Authorization: clientAuth],
				uri: "https://api.particle.io/oauth/token",
				body: [grant_type: "password", 
        			username: particleUsername,
        			password: particlePassword
                	] 
	        ]
	   		try {
	        	httpPost(params) {response -> 
    	    		state.particleToken = response.data.access_token
        	       	}
                log.debug "Created new Particle.io token"        
	        	//log.debug "Particle Token ${state.particleToken}"
	        	checkToken()
				return true
			} catch (e) {
    	    	log.error "error: $e"
                return false
        	}
 
		}
        else {
        	return true
        }
}

// Check to see if we need to make the webhook or if it is there already
void checkWebhook() {
	int foundHook = 0
    try {
		httpGet(uri:"https://api.particle.io/v1/webhooks?access_token=${state.particleToken}",
	    	) {response -> response.data.each { 
	    		hook ->
					//log.debug hook.event
	                if (hook.event == state.webhookName) {
	                	foundHook = 1
	                	log.debug "Found existing webhook id: ${hook.id}"                    
 	               	}
	           		}
	 	   			if (!foundHook) {
	    				createWebhook()
	    			}
	    			else {
	    			}
			}
 	}
 	catch (e) {
   		log.error "error: $e"
    }
}

// Create a new particle webhook for this app to use
void createWebhook() {
	log.debug "Creating a Particle webhook "
    try {         
		httpPost(uri: "https://api.particle.io/v1/webhooks",
      			body: [access_token: state.particleToken, 
        		event: state.webhookName,
                url: state.appURL, 
                requestType: "PUT", 
                mydevices: true]
      			) {response -> log.debug (response.data)}
    }
 	catch (e) {
   		log.error "error: $e"
    }
}

// get a list of the particle devices from the account
def getDevices() {
	def particleDevices = [:]
	//Particle API Call
    try {
	    def readingClosure = { response -> response.data.each { core ->
    		particleDevices.put(core.id, core.name)  
        	}
		}
	    httpGet("https://api.particle.io/v1/devices?access_token=${state.particleToken}", readingClosure)
    }
 	catch (e) {
   		log.error "error: $e"
    }
 	return particleDevices
}

// Is there an accessToken yet?
def checkToken() {
	if (!state.accessToken) {
    	createAccessToken() 
    }
}

def poll_devices() {
 alldevices.poll()
}

mappings {
    path("/motion/:data/:deviceid") {
        action: [
            PUT: "motionevent"
        ]
    }
}

// send an on command to the particle device
def switchOnHandler(evt) {
	evt.getProperties().each {e -> log.debug "${e}" }
    log.debug "switch turned on!"
    def pinToSet = "D0"
    try {
    	httpPost("https://api.particle.io/v1/devices/${particleDevice}/setOn?access_token=${state.particleToken}","command=${pinToSet}",) {response -> log.debug (response.data)}
	}
 	catch (e) {
   		log.error "error: $e"
    }
}

// send an off command to the particle device
def switchOffHandler(evt) {
    log.debug "switch turned off!"
    def pinToSet = "D0"
    try {
    	httpPost("https://api.particle.io/v1/devices/${particleDevice}/setOff?access_token=${state.particleToken}","command=${pinToSet}",) {response -> log.debug (response.data)}
	}
 	catch (e) {
   		log.error "error: $e"
    }
}

// send a value to particle device
def switchValueHandler(evt) {
    log.debug "switch dimmed to ${evt.value}!"
    def pinToSet = "D0"
    def pinValue = "101"
    try {
    	httpPost("https://api.particle.io/v1/devices/${particleDevice}/setValue?access_token=${state.particleToken}","command=${pinToSet}:${pinValue}",) {response -> log.debug (response.data)}
	}
 	catch (e) {
   		log.error "error: $e"
    }
}

void huechange() {
 try {
      httpPost (uri: "https://api.particle.io/v1/devices/events",
        body: [access_token: state.particleToken,
        name: "ghmcmd",
        private: false,
        data: "H${state.ghue}" ] ) {response -> log.debug "HUE change sent: H${state.ghue}, Response: ${response.data}" }
        }
     catch (e) {
   		log.error "error: $e"
    }
    state.ghue = state.ghue + 1
    if (state.ghue == 9) state.ghue = 1
}

// got the webhook from the particle device now do something with the information
def motionevent() {
    alldevices.command(params.param3, params.param2)
    //particleDevice.command("abc","abc")
	log.debug "Got motion data: ${params.param3}, ${params.param2}"
    return [Respond: "OK"]
}

def uninstalled() {
  log.debug "Uninstalling ParticleThings"
  deleteWebhook()
  deleteAccessToken()
  log.debug "ParticleThings Uninstalled"
}

// Cleanup on uninstall - remove the particle webhook for this app
void deleteWebhook() {
try{
httpGet(uri:"https://api.particle.io/v1/webhooks?access_token=${state.particleToken}",
    ) {response -> response.data.each { 
    		hook ->
				//log.debug hook.event
                if (hook.event == state.webhookName) {
                	httpDelete(uri:"https://api.particle.io/v1/webhooks/${hook.id}?access_token=${state.particleToken}")
                    log.debug "Deleted the existing webhook with the id: ${hook.id} and the event name: ${state.webhookName}"
                }
           }
 }
 } catch (all) {log.debug "Couldn't delete webhook, moving on"}
}

// Cleanup on uninstall - remove the particle access token
void deleteAccessToken() {
	try{
    	def clientAuth = "${particleUsername}:${particlePassword}"
	   	clientAuth = "Basic " + clientAuth.encodeAsBase64().toString()
	   	def params = [
	        headers: [Authorization: clientAuth],
			uri: "https://api.particle.io/v1/access_tokens/${state.particleToken}"
	        ]
	httpDelete(params) //uri:"https://${particleUsername}:${particlePassword}@api.particle.io/v1/access_tokens/${state.particleToken}")
	log.debug "Deleted the existing Particle Access Token"
 } catch (e) {log.debug "Couldn't delete Particle Token, moving on: $e"}
}
