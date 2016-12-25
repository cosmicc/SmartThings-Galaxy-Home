/**
 *  Galaxy Home Module Device Handler
 *
 *  Copyright 2016 cosmicc
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */
 
preferences {
    input("deviceId", "text", title: "Device ID")
    input("token", "text", title: "Access Token")
}

metadata {
	definition (name: "Galaxy Home Module Device Handler", namespace: "cosmicc", author: "cosmicc", oauth: true) {
		//capability "Battery"
		//capability "Carbon Monoxide Detector"
        capability "Switch"
  		capability "SwitchLevel"
		capability "Actuator"
		capability "Color Control"
        //capability "Bulb"
		capability "Color Temperature"
		capability "Configuration"
		capability "Health Check"
		capability "Light"
		capability "Motion Sensor"
		capability "Polling"
		capability "Refresh"
        capability "Beacon"
		//capability "Relative Humidity Measurement"
		capability "Sensor"
		capability "Signal Strength"
		capability "Smoke Detector"
		//capability "Temperature Measurement"
		capability "Test Capability"
		//capability "Thermostat"
        
    attribute "version", "number"
    attribute "leds", "number"
    attribute "uptime", "string"
    attribute "lastrestart", "string"
    attribute "systemmode", "number"
    attribute "pricolor", "string"
    attribute "seccolor", "string"
    attribute "motionlight", "enum", ["true", "false"]
    attribute "sleeping", "enum", ["true", "false"]
    attribute "animspeed", "number"
    attribute "brightness", "number"
    attribute "connected", "enum", ["true", "false"]
    attribute "lastmotion", "number"
    attribute "brightness", "number"
    attribute "fltemp", "number"
    
    
    command "command", ["string", "string"]
    command "motionLight"
    command "sysmodeUp"
    command "sysmodeDown"
    command "setAdjustedColor"
    command "setAnimSpeed"
	}

	simulator {
		// TODO: define status and reply messages here
	}
   
	tiles (scale: 2) {
    multiAttributeTile(name:"switch", type: "generic", width: 6, height: 4, canChangeIcon: true) {
    tileAttribute ("device.switch", key: "PRIMARY_CONTROL") {
        attributeState "on", label:'${name}', action:"off", icon:"st.lights.philips.hue-single", backgroundColor:"#79b821", nextState:"turningOff"
        attributeState "off", label:'${name}', action:"on", icon:"st.lights.philips.hue-single", backgroundColor:"#ffffff", nextState:"turningOn"
        attributeState "turningOn", label:'${name}', action:"off", icon:"st.lights.philips.hue-single", backgroundColor:"#79b821", nextState:"turningOff"
        attributeState "turningOff", label:'${name}', action:"on", icon:"st.lights.philips.hue-single", backgroundColor:"#ffffff", nextState:"turningOn"
    }
    tileAttribute ("device.systemmode", label:'${device.systemmode}', key: "VALUE_CONTROL") {
         attributeState("systemmode", action: "sysmodeUp")
        attributeState("systemmode", action: "sysmodeDown")
    }
    tileAttribute ("device.animspeed", key: "SLIDER_CONTROL") {
        attributeState "animspeed", action:"setAnimSpeed"
    }
    tileAttribute ("device.color", key: "COLOR_CONTROL") {
        attributeState "color", action:"setAdjustedColor"
    }
        tileAttribute ("device.leds", key: "SECONDARY_CONTROL") {
        attributeState "leds", label:'${currentValue} Leds'
        }
}

    
        standardTile("connected", "device.connected", width: 1, height: 1){
            state "false", label: "OFFLINE", backgroundColor: "#B9B9B9"
       		state "true", label: "ONLINE", backgroundColor: "#00B900"
            
		}
    	standardTile("version", "device.version", width: 1, height: 1){
            state "version", label: '${currentValue}', unit:""
		}
        
        standardTile("lastrestart", "device.lastrestart", width: 3, height: 1){
            state "lastrestart", label: 'Last Restart: ${currentValue}', unit:""
		}
        standardTile("uptime", "device.uptime", width: 3, height: 1){
            state "uptime", label: 'Uptime: ${currentValue}', unit:""
		}
            	standardTile("lastmotion", "device.lastmotion", width: 3, height: 1){
            state "lastmotion", label: 'Last Motion: ${currentValue} Min ago', unit:"Minutes"
		}
            	standardTile("motionlight", "device.motionlight", width: 1, height: 1){
            state "false", label: "LOM: OFF", action: "motionLight", backgroundColor: "B9B9B9"
       		state "true", label: "LOM: ON", action: "motionLight", backgroundColor: "#00B900"
		}
            	standardTile("seccolor", "device.seccolor", width: 1, height: 1){
            state "seccolor", label: 'Secondary Color: ${currentValue}', unit:""
		}
        standardTile("motion", "device.motion", width: 2, height: 1) {
    state "active", label: 'Motion ${currentValue}', backgroundColor: "#00b900"
    state "inactive", label: 'Motion ${currentValue}', backgroundColor: "B9B9B9"
}
		valueTile("temperature", "device.temperature", width: 1, height: 1){
            state "temperature", label: '${currentValue}°F', unit:"",
            	backgroundColors: [
                	[value: 65, color: "#000090"],
                    [value: 72, color: "#009000"],
                    [value: 75, color: "#009000"],
                    [value: 82, color: "#900000"]
                ]
		}
        
        valueTile("humidity", "device.humidity", width: 1, height: 1){
            state "humidity", label: '${currentValue}%', unit:"",
            	backgroundColors: [
                    [value: 20, color: "#000090"],
                    [value: 30, color: "#009000"],
                    [value: 50, color: "#009000"],
                    [value: 70, color: "#900000"]
                ]
		}

		valueTile("fltemp", "device.fltemp", width: 1, height: 1){
            state "fltemp", label: '${currentValue}°F', unit:"",
            	backgroundColors: [
                	[value: 65, color: "#000090"],
                    [value: 72, color: "#009000"],
                    [value: 75, color: "#009000"],
                    [value: 82, color: "#900000"]
                ]
		}
                valueTile("signal", "device.signal", width: 1, height: 1){
            state "signal", label: '${currentValue}db', unit:"db",
            	backgroundColors: [
                    [value: -40, color: "#00FF00"],
                    [value: -85, color: "#FF0000"]
                ]
		}
        
        standardTile("refresh", "device.temperature", inactiveLabel: false, decoration: "flat") {
            state "default", action:"polling.poll", icon:"st.secondary.refresh"
        }
        
        main "connected"
		details(["switch","version","signal","connected","temperature","humidity","fltemp","motionlight","motion","lastmotion","lastrestart","uptime","refresh"])
	}
}

def ping() {
	return 1 
}

// handle commands
def poll() {
    def connected = ParticleCheckAlive()
    if (connected == true) {
    log.debug "Retrieving Particle Data for ${deviceId}"
    ParticleVar("version")
    ParticleVar("leds")
    ParticleVar("brightness")
    ParticleVar("lastrestart")
    ParticleVar("uptime")
    ParticleVar("systemmode")
    ParticleVar("sleeping")
    ParticleVar("signal")
    ParticleVar("lastmotion")
    ParticleVar("animspeed")
    ParticleVar("pricolor")
    ParticleVar("seccolor")
    ParticleVar("temperature")
    ParticleVar("humidity")
    ParticleVar("fltemp")
    ParticleVar("motionlight")
    }
}

def command(String a, String b) {
 if (a == deviceId) {
   log.debug "Device ${deviceId} setting motion: ${b}"
   if (b == "motion_started") sendEvent(name: "motion", value: "active")
   else if (b == "motion_ended") sendEvent(name: "motion", value: "inactive")
 }
}

def sendCmd(String cmd) {
    log.debug "Sending command to particle device: $cmd"
    def commandClosure = { response ->
    if (response.data.return_value != 0) log.debug "Command send successful, $response.data"
      else log.debug "Command send un-successful!, $response.data"
	}
    def commandParams = [
  		uri: "https://api.particle.io/v1/devices/${deviceId}/command",
        body: [access_token: token, arg: cmd],
        success: commandClosure
	]
	httpPost(commandParams)
}

def motionLight() {
  sendCmd("LMC")
}

def sysmodeUp() {
 sendCmd("S+")
}

def sysmodeDown() {
 sendCmd("S-")
}

def ParticleCheckAlive() {
    def params = [
        uri:  "https://api.particle.io/v1/devices/${deviceId}/?access_token=${token}",
        contentType: 'application/json',
    ]
    try {
        httpGet(params) {resp ->
        sendEvent(name: "connected", value: resp.data.connected)
        sendEvent(name: "lastheard", value: resp.data.last_heard)
        sendEvent(name: "name", value: resp.data.name)
        if (resp.data.connected == true) {
        log.debug "Device ${resp.data.name} is Connected"
        return true;
        } else {
        log.debug "Device ${resp.data.name} is Disconnected"
        return false;
        }
        }
    } catch (e) {
        log.error "error: $e"
    }
}

def ParticleVar(String parvar) {
    def params = [
        uri:  "https://api.particle.io/v1/devices/${deviceId}/${parvar}?access_token=${token}",
        contentType: 'application/json',
    ]
    try {
        httpGet(params) {resp ->
        sendEvent(name: parvar, value: resp.data.result)
        
        //log.debug "resp data: ${resp.data}"
        //log.debug "${parvar}: ${resp.data.result}"
        }
    } catch (e) {
        log.error "error: $e"
    }
}

def motionevt() {
  if (devid == deviceId) {
    log.debug "Motion Event ${evtdata}"
    if(evtdata == "started") sendEvent(name: motion, value: true)
    else if (evtdata == "stopped") sendEvent(name: motion, value: false)
  }  
}

private pullData() {
    def temperatureClosure = { response ->
	  	log.debug "Temeprature Request was successful, $response.data"
      
      	sendEvent(name: "temperature", value: response.data.return_value)
	}
    def temperatureParams = [
  		uri: "https://api.particle.io/v1/devices/${deviceId}/getTemp",
        body: [access_token: token],  
        success: temperatureClosure
	]
	httpPost(temperatureParams)
    
    def humidityClosure = { response ->
	  	log.debug "Humidity Request was successful, $response.data"
      
      	sendEvent(name: "humidity", value: response.data.return_value)
	}
    def humidityParams = [
  		uri: "https://api.particle.io/v1/devices/${deviceId}/getHum",
        body: [access_token: token],  
        success: humidityClosure
	]
	httpPost(humidityParams)
    
        def signalClosure = { response ->
	  	log.debug "Signal Request was successful, $response.data"
      
      	sendEvent(name: "signal", value: response.data.return_value)
	}
    def signalParams = [
  		uri: "https://api.particle.io/v1/devices/${deviceId}/getSig",
        body: [access_token: token],  
        success: signalClosure
	]
	httpPost(signalParams)
    
}

// parse events into attributes
def parse(String description) {
	log.debug "Parsing '${description}'"
	// TODO: handle 'battery' attribute
	// TODO: handle 'carbonMonoxide' attribute
	// TODO: handle 'hue' attribute
	// TODO: handle 'saturation' attribute
	// TODO: handle 'color' attribute
	// TODO: handle 'colorTemperature' attribute
	// TODO: handle 'checkInterval' attribute
	// TODO: handle 'switch' attribute
	// TODO: handle 'motion' attribute
	// TODO: handle 'humidity' attribute
	// TODO: handle 'lqi' attribute
	// TODO: handle 'rssi' attribute
	// TODO: handle 'smoke' attribute
	// TODO: handle 'temperature' attribute
	// TODO: handle 'temperature' attribute
	// TODO: handle 'heatingSetpoint' attribute
	// TODO: handle 'coolingSetpoint' attribute
	// TODO: handle 'thermostatSetpoint' attribute
	// TODO: handle 'thermostatMode' attribute
	// TODO: handle 'thermostatFanMode' attribute
	// TODO: handle 'thermostatOperatingState' attribute
	// TODO: handle 'schedule' attribute

}

def setLevel(value) {
  log.debug "Brightness: ${value}"
}

// handle commands
def setHue() {
	log.debug "Executing 'setHue'"
	// TODO: handle 'setHue' command
}

def setSaturation() {
	log.debug "Executing 'setSaturation'"
	// TODO: handle 'setSaturation' command
}

def setAnimSpeed(value) {
  log.debug "Sending Animation Speed: ${value}%"
  sendCmd("N${value}")
}

def setAdjustedColor(value) {
    log.debug "${value}"
    setColorRGB(value.hex)
    }

def setColorRGB(value) {
	log.debug "Device: ${deviceId} SetColor: ${value}"
    sendCmd(value)
}

def setColor(value) {
	log.debug "Device: ${deviceId} SetColor: ${value}"
    String hhue = Integer.toHexString(value.hue);
    if (hhue.length() == 1) hhue = "0"+hhue;
    String hsat = Integer.toHexString(value.saturation);
    log.debug "${hhue}${hsat}"
    sendCmd("@${hhue}${hsat}")
}

def setColorTemperature() {
	log.debug "Executing 'setColorTemperature'"
	// TODO: handle 'setColorTemperature' command
}

def configure() {
	log.debug "Executing 'configure'"
	// TODO: handle 'configure' command
}

def off() {
	log.debug "Executing 'off'"
    sendEvent(name: "switch", value: "off")
	sendCmd("P0")
}

def on() {
	log.debug "Executing 'on'"
     sendEvent(name: "switch", value: "on")
	sendCmd("P1")
}

def refresh() {
	log.debug "Executing 'refresh'"
	// TODO: handle 'refresh' command
}

def setHeatingSetpoint() {
	log.debug "Executing 'setHeatingSetpoint'"
	// TODO: handle 'setHeatingSetpoint' command
}

def setCoolingSetpoint() {
	log.debug "Executing 'setCoolingSetpoint'"
	// TODO: handle 'setCoolingSetpoint' command
}

def heat() {
	log.debug "Executing 'heat'"
	// TODO: handle 'heat' command
}

def emergencyHeat() {
	log.debug "Executing 'emergencyHeat'"
	// TODO: handle 'emergencyHeat' command
}

def cool() {
	log.debug "Executing 'cool'"
	// TODO: handle 'cool' command
}

def setThermostatMode() {
	log.debug "Executing 'setThermostatMode'"
	// TODO: handle 'setThermostatMode' command
}

def fanOn() {
	log.debug "Executing 'fanOn'"
	// TODO: handle 'fanOn' command
}

def fanAuto() {
	log.debug "Executing 'fanAuto'"
	// TODO: handle 'fanAuto' command
}

def fanCirculate() {
	log.debug "Executing 'fanCirculate'"
	// TODO: handle 'fanCirculate' command
}

def setThermostatFanMode() {
	log.debug "Executing 'setThermostatFanMode'"
	// TODO: handle 'setThermostatFanMode' command
}

def auto() {
	log.debug "Executing 'auto'"
	// TODO: handle 'auto' command
}

def setSchedule() {
	log.debug "Executing 'setSchedule'"
	// TODO: handle 'setSchedule' command
}