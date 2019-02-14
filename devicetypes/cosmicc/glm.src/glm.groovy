/**
 *  Galaxy Lighting Module
 *
 *  Copyright 2019 Ian Perry
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
import groovy.json.JsonSlurper
import com.google.common.base.Splitter;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

preferences {
        input("ip", "string", title:"IP Address", description: "192.168.1.150", defaultValue: "192.168.1.150" ,required: true, displayDuringSetup: true)
        input("port", "string", title:"Port", description: "80", defaultValue: "80" , required: true, displayDuringSetup: true)
        input("token", "string", title:"API Token", description: "apitoken", defaultValue: "0000000" , required: true, displayDuringSetup: true)
}

metadata {
	definition (name: "GLM", namespace: "cosmicc", author: "Ian Perry") {
		capability "Activity Lighting Mode"
		capability "Activity Sensor"
		capability "Beacon"
		capability "Bulb"
		capability "Color"
		capability "Color Control"
		capability "Color Mode"
		capability "Color Temperature"
		capability "Configuration"
		capability "Health Check"
		capability "Illuminance Measurement"
		capability "Light"
		capability "Location Mode"
		capability "Motion Sensor"
		capability "Occupancy Sensor"
		capability "Polling"
		capability "Presence Sensor"
		capability "Refresh"
		capability "Relative Humidity Measurement"
		capability "Sensor"
		capability "Signal Strength"
		capability "Switch"
		capability "Switch Level"
		capability "Temperature Measurement"

		attribute "cpu_temp", "string"

		command "reset"
	}


	simulator {
		// TODO: define status and reply messages here
	}

tiles {
	valueTile("currentColor", "device.color") {
		state "color", label: '${currentValue}', defaultState: true
	}
	controlTile("rgbSelector", "device.color", "color", height: 4, width: 4,
            inactiveLabel: false) {
    state "color", action: "color control.setColor"
	}
	valueTile("temperature", "device.temperature", width: 1, height: 1) {
            state "temperature", label:'${currentValue}° F', unit: "F",
            backgroundColors:[
                [value: 45, color: "#153591"],
                [value: 55, color: "#1e9cbb"],
                [value: 60, color: "#90d2a7"],
                [value: 65, color: "#44b621"],
                [value: 80, color: "#f1d801"],
                [value: 85, color: "#d04e00"],
                [value: 90, color: "#bc2323"]
            ]
        }
        standardTile("button", "device.switch", width: 1, height: 1, canChangeIcon: true) {
			state "off", label: 'Off', icon: "st.Electronics.electronics18", backgroundColor: "#ffffff", nextState: "on"
			state "on", label: 'On', icon: "st.Electronics.electronics18", backgroundColor: "#79b821", nextState: "off"
		}
        valueTile("humidity", "device.humidity", width: 1, height: 1) {
            state "humidity", label:'${currentValue}%',
            backgroundColors:[
                [value: 0, color: "#153591"],
                [value: 10, color: "#1e9cbb"],
                [value: 25, color: "#90d2a7"],
                [value: 40, color: "#44b621"],
                [value: 60, color: "#f1d801"],
                [value: 70, color: "#d04e00"],
                [value: 80, color: "#bc2323"]
            ]
        }
        valueTile("cpu_temp", "device.cpu_temp", width: 1, height: 1) {
            state "cpu_temp", label:'${currentValue}° ', unit: "F",
            backgroundColors:[
                [value: 70, color: "#153591"],
                [value: 75, color: "#1e9cbb"],
                [value: 85, color: "#90d2a7"],
                [value: 95, color: "#44b621"],
                [value: 105, color: "#f1d801"],
                [value: 120, color: "#d04e00"],
                [value: 140, color: "#bc2323"]
            ]
        }
        standardTile("restart", "device.restart", inactiveLabel: false, decoration: "flat") {
        	state "default", action:"restart", label: "Restart", displayName: "Restart"
        }
        standardTile("refresh", "device.refresh", inactiveLabel: false, decoration: "flat") {
        	state "default", action:"refresh.refresh", icon: "st.secondary.refresh"
        }
        main "currentColor"
        details(["rgbSelector"])
        //details(["button", "rgbselector", "temperature", "humidity", "cpu_temp", "restart", "refresh"])
    }
}

// parse events into attributes
def parse(String description) {
	log.debug "Parsing '${description}'"
    def map = [:]
    def descMap = parseDescriptionAsMap(description)
    log.debug "descMap: ${descMap}"
    
    def body = new String(descMap["body"].decodeBase64())
    log.debug "body: ${body}"
    
    def slurper = new JsonSlurper()
    def result = slurper.parseText(body)
    
    log.debug "result: ${result}"

	if (result){
    	log.debug "Device is ALIVE"
   		//sendEvent(name: "switch", value: "on")
    }
    if (result.containsKey("temperature")) {
    log.debug "temperature: ${result.temperature}"
    sendEvent(name: "temperature", value: result.temperature)
    }
    if (result.containsKey("humidity")) {
    log.debug "humidity: ${result.humidity}"
    sendEvent(name: "humidity", value: result.humidity)
    }
    if (result.containsKey("rssi")) {
    log.debug "rssi: ${result.rssi}"
    sendEvent(name: "rssi", value: result.rssi)
    }
    if (result.containsKey("lqi")) {
    log.debug "link quality: ${result.lqi}"
    sendEvent(name: "lqi", value: result.lqi)
    }
    if (result.containsKey("cpu_temp")) {
    log.debug "cpu temp: ${result.cpu_temp}"
    sendEvent(name: "cpu_temp", value: result.cpu_temp)
    }
	// TODO: handle 'lightingMode' attribute
	// TODO: handle 'activity' attribute
	// TODO: handle 'presence' attribute
	// TODO: handle 'colorValue' attribute
	// TODO: handle 'hue' attribute
	// TODO: handle 'saturation' attribute
	// TODO: handle 'color' attribute
	// TODO: handle 'colorMode' attribute
	// TODO: handle 'colorTemperature' attribute
	// TODO: handle 'checkInterval' attribute
	// TODO: handle 'DeviceWatch-DeviceStatus' attribute
	// TODO: handle 'healthStatus' attribute
	// TODO: handle 'healthStatus' attribute
	// TODO: handle 'illuminance' attribute
	// TODO: handle 'switch' attribute
	// TODO: handle 'mode' attribute
	// TODO: handle 'motion' attribute
	// TODO: handle 'occupancy' attribute
	// TODO: handle 'presence' attribute
	// TODO: handle 'switch' attribute
	// TODO: handle 'level' attribute

}

// handle commands
def setLightingMode() {
	log.debug "Executing 'setLightingMode'"
	// TODO: handle 'setLightingMode' command
}

def off() {
	log.debug "Executing 'off'"
    sendEvent(name: "switch", value: "off")
	postAction("PUT", "/api/enable?enable=off")
}

def on() {
	log.debug "Executing 'on'"
    sendEvent(name: "switch", value: "on")
	postAction("PUT", "/api/enable?enable=on")
}

def setColorValue() {
	log.debug "Executing 'setColorValue'"
	// TODO: handle 'setColorValue' command
}

def setHue() {
	log.debug "Executing 'setHue'"
	// TODO: handle 'setHue' command
}

def setSaturation() {
	log.debug "Executing 'setSaturation'"
	// TODO: handle 'setSaturation' command
}

def setColor(value) {
	log.trace "Executing 'setColor' ${value}"
	postAction("PUT", "/api/hsvcolor?hue=${value.hue}&saturation=${value.saturation}&lightness=100")
}

def setColorTemperature() {
	log.debug "Executing 'setColorTemperature'"
	// TODO: handle 'setColorTemperature' command
}

def configure() {
	log.debug "Executing 'configure'"
	// TODO: handle 'configure' command
}

def ping() {
	log.debug "Executing 'ping'"
	// TODO: handle 'ping' command
}

def setMode() {
	log.debug "Executing 'setMode'"
	// TODO: handle 'setMode' command
}

def poll() {
	log.debug "Executing 'poll'"
	postAction("GET", "/api/poll")
}

def refresh() {
	log.debug "Executing 'refresh'"
	postAction("GET", "/api/info")
}

def setLevel() {
	log.debug "Executing 'setLevel'"
	// TODO: handle 'setLevel' command
}

def reset() {
	log.debug "Executing 'reset'"
	// TODO: handle 'reset' command
}

def postAction(meth, uri) {
    def result = new physicalgraph.device.HubAction(
        method: meth,
        path: uri,
        headers: [
            HOST: getHostAddress()
        ]
    )
    log.debug "postAction: ${result}"
    return result
}

def parseDescriptionAsMap(description) {
	description.split(",").inject([:]) { map, param ->
		def nameAndValue = param.split(":")
		map += [(nameAndValue[0].trim()):nameAndValue[1].trim()]
	}
}


def toAscii(s){
        StringBuilder sb = new StringBuilder();
        String ascString = null;
        long asciiInt;
                for (int i = 0; i < s.length(); i++){
                    sb.append((int)s.charAt(i));
                    sb.append("|");
                    char c = s.charAt(i);
                }
                ascString = sb.toString();
                asciiInt = Long.parseLong(ascString);
                return asciiInt;
    }

private encodeCredentials(username, password){
	log.debug "Encoding credentials"
	def userpassascii = "${username}:${password}"
    def userpass = "Basic " + userpassascii.encodeAsBase64().toString()
    //log.debug "ASCII credentials are ${userpassascii}"
    //log.debug "Credentials are ${userpass}"
    return userpass
}

private getHeader(userpass){
	log.debug "Getting headers"
    def headers = [:]
    headers.put("HOST", getHostAddress())
    headers.put("Authorization", userpass)
    //log.debug "Headers are ${headers}"
    return headers
}

private delayAction(long time) {
	new physicalgraph.device.HubAction("delay $time")
}

private setDeviceNetworkId(ip,port){
  	def iphex = convertIPtoHex(ip)
  	def porthex = convertPortToHex(port)
  	device.deviceNetworkId = "$iphex:$porthex"
  	log.debug "Device Network Id set to ${iphex}:${porthex}"
}

private getHostAddress() {
	return "${ip}:${port}"
}

private String convertIPtoHex(ipAddress) { 
    String hex = ipAddress.tokenize( '.' ).collect {  String.format( '%02x', it.toInteger() ) }.join()
    return hex

}

private String convertPortToHex(port) {
	String hexport = port.toString().format( '%04x', port.toInteger() )
    return hexport
}
