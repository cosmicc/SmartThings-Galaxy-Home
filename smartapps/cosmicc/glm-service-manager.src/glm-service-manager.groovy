/**
 *  GLM Service Manager
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
definition(
    name: "GLM Service Manager",
    namespace: "cosmicc",
    author: "Ian Perry",
    description: "Galaxy Lighting Module Service Manager",
    category: "SmartThings Labs",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")


preferences {
	section("Title") {
		input "glms", "capability.presenceSensor", multiple: true
        input "colorlights", "capability.colorControl", multiple: true
	}
    
}

def installed() {
	log.debug "Installed with settings: ${settings}"

	initialize()
}

def updated() {
	log.debug "Updated with settings: ${settings}"

	unsubscribe()
	initialize()
}

def initialize() {
	// TODO: subscribe to attributes, devices, locations, etc.
    state.ghue = 0
    schedule("*/2 * * * * ?", poll_devices)
    schedule("* * * * * ?", huwchange)
}

// TODO: implement event handlers

def poll_devices() {
 glms.poll()
}

void huechange() {
 	colorlights.setColor(['hue': state.ghue, 'saturation': 100])
    state.ghue = state.ghue + 1
    if (state.ghue == 100) state.ghue = 0
}
