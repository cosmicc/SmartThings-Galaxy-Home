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
		capability "Battery"
		capability "Carbon Monoxide Detector"
		capability "Color Control"
		capability "Color Temperature"
		capability "Configuration"
		capability "Health Check"
		capability "Light"
		capability "Motion Sensor"
		capability "Polling"
		capability "Refresh"
		capability "Relative Humidity Measurement"
		capability "Sensor"
		capability "Signal Strength"
		capability "Smoke Detector"
		capability "Temperature Measurement"
		capability "Test Capability"
		capability "Thermostat"
	}


	simulator {
		// TODO: define status and reply messages here
	}

	tiles {
		valueTile("temperature", "device.temperature", width: 2, height: 2){
            state "temperature", label: '${currentValue}°F', unit:"",
            	backgroundColors: [
                    [value: 25, color: "#202040"],
                    [value: 30, color: "#202080"]
                ]
		}
        
        valueTile("humidity", "device.humidity", width: 2, height: 2){
            state "humidity", label: '${currentValue}%', unit:"",
            	backgroundColors: [
                    [value: 50, color: "#202040"],
                    [value: 80, color: "#202080"]
                ]
		}
                valueTile("signal", "device.signal", width: 1, height: 1){
            state "signal", label: '${currentValue}db', unit:"",
            	backgroundColors: [
                    [value: 50, color: "#202040"],
                    [value: 80, color: "#202080"]
                ]
		}
        
        standardTile("refresh", "device.temperature", inactiveLabel: false, decoration: "flat") {
            state "default", action:"polling.poll", icon:"st.secondary.refresh"
        }
        
        main "temperature"
		details(["temperature", "humidity", "signal", "refresh"])
	}
}

def ping() {
	return 1 
}

// handle commands
def poll() {
	log.debug "Executing 'poll'"
    pullData()
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

// handle commands
def setHue() {
	log.debug "Executing 'setHue'"
	// TODO: handle 'setHue' command
}

def setSaturation() {
	log.debug "Executing 'setSaturation'"
	// TODO: handle 'setSaturation' command
}

def setColor() {
	log.debug "Executing 'setColor'"
	// TODO: handle 'setColor' command
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
	// TODO: handle 'off' command
}

def on() {
	log.debug "Executing 'on'"
	// TODO: handle 'on' command
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