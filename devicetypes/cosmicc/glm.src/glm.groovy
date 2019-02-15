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
		//capability "Color"
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

	tiles(scale: 2) {
		valueTile("currentColor", "device.color") {
			state "color", label: '${currentValue}', defaultState: true
		}

		controlTile("rgbSelector", "device.color", "color", height: 6, width: 6, inactiveLabel: false) {
			state "color", action: "color control.setColor"
		}

		main("currentColor")
		details([
			"rgbSelector"
		])
	}
}

// parse events into attributes
def parse(String description) {
	//log.debug "Parsing '${description}'"
    def map = [:]
    def descMap = parseDescriptionAsMap(description)
    //log.debug "descMap: ${descMap}"
    
    def body = new String(descMap["body"].decodeBase64())
    log.debug "body: ${body}"
    
    def slurper = new JsonSlurper()
    def result = slurper.parseText(body)
    
    log.debug "result: ${result}"

	if (result){
    	//log.debug "Device is ALIVE"
   		//sendEvent(name: "switch", value: "on")
    }
    if (result.containsKey("temperature")) {
    //log.debug "temperature: ${result.temperature}"
    sendEvent(name: "temperature", value: result.temperature)
    }
    if (result.containsKey("humidity")) {
    //log.debug "humidity: ${result.humidity}"
    sendEvent(name: "humidity", value: result.humidity)
    }
    if (result.containsKey("rssi")) {
    //log.debug "rssi: ${result.rssi}"
    sendEvent(name: "rssi", value: result.rssi)
    }
    if (result.containsKey("lqi")) {
    //log.debug "link quality: ${result.lqi}"
    sendEvent(name: "lqi", value: result.lqi)
    }
    if (result.containsKey("cpu_temp")) {
    //log.debug "cpu temp: ${result.cpu_temp}"
    sendEvent(name: "cpu_temp", value: result.cpu_temp)
    }
    if (result.containsKey("colortemp")) {
    //log.debug "white temp: ${result.colortemp}"
    sendEvent(name: "colorTemperature", value: result.colortemp)
    }
    if (result.containsKey("color")) {
    log.debug "color: ${result.color}"
    sendEvent(name: "color", value: result.color)
    }
	// TODO: handle 'lightingMode' attribute
	// TODO: handle 'activity' attribute
	// TODO: handle 'presence' attribute
	// TODO: handle 'colorValue' attribute
	// TODO: handle 'hue' attribute
	// TODO: handle 'saturation' attribute
	// TODO: handle 'colorMode' attribute
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

def setColorValue(value) {
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
	log.debug "Executing 'setColor' ${value}"
	postAction("PUT", "/api/hsvcolor?hue=${value.hue}&saturation=${value.saturation}")
}

def setColorTemperature(value) {
	log.debug "Executing 'setColorTemperature'"
	postAction("PUT", "/api/whitetemp?kelvin=${value}")
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

def setLevel(value) {
	log.debug "Executing 'setLevel'"
	postAction("PUT", "/api/brightness?brightness=${value}")
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
    log.trace "postAction: ${result}"
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
