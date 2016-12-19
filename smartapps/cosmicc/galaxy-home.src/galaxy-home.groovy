/**
 *  Galaxy Home
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
definition(
    name: "Galaxy Home",
    namespace: "cosmicc",
    author: "cosmicc",
    description: "Galaxy Home SpartApp",
    category: "Convenience",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    oauth: true)


preferences {
    page(name: "mainPage")
}

def mainPage() {
    dynamicPage(name: "mainPage", title: "Test Parent Child Device App", install: true, uninstall: true) {
        // Let the user know the current status
        section("Status") {
            def devices = getChildDevices().each { device ->
                log.trace "Found child ${device.displayName}"
                def stuff = device.readStuff()
                paragraph "${device.displayName}: $stuff"
            }
        }

        def physicalHubs = location.hubs.findAll { it.type == physicalgraph.device.HubType.PHYSICAL } // Ignore Virtual hubs
        if (physicalHubs.size() > 1) { // If there is more than one hub then select the hub otherwise we'll the default hub
            section("Hub Selection") {
                paragraph title: "", "Multiple SmartThings Hubs have been detected at this location. Please select the Hub."
                input name: "installHub", type: "hub", title: "Select the Hub", required: true
            }
        }
    }
}

def installed()
{
    log.debug "Installed: $settings"
    initialize()
}

def updated()
{
    log.debug "Updated: $settings"

    unsubscribe()
    unschedule()
    initialize()
}

def uninstalled() {
    log.trace "Uninstalled called"
    getChildDevices().each {device ->
        log.info "Deleting Device $device.displayName"
        deleteChildDevice(device.deviceNetworkId)
    }
}

def initialize() {
    def physicalHubs = location.hubs.findAll { it.type == physicalgraph.device.HubType.PHYSICAL } // Ignore Virtual hubs
    log.trace "Selected Hub ID ${installHub?.id}, All Hubs Types: ${location.hubs*.type}, Names: ${location.hubs*.name}, IDs: ${location.hubs*.id}, IPs: ${location.hubs*.localIP}, Total Hubs Found: ${location.hubs.size()}, Physical Hubs Found: ${physicalHubs.size()}"

    try {
        def existingDevices = getChildDevices()
        log.trace "Found devices $existingDevices"
        if(!existingDevices) {
            if ((physicalHubs.size() > 1) && !installHub) {
                log.error "Found more than one physical hub and user has NOT selected a hub in the SmartApp settings"
                throw new RuntimeException("Select Hub in SmartApp settings") // Lets not continue with out this settings
            }
            if (physicalHubs.size() < 1) {
                log.error "NO Physical hubs found at this location, please contact SmartThings support!"
                throw new RuntimeException("No physical hubs found") // Lets not continue with out this settings
            }
            
            (1..5).each {
                def id = 3000 + it
                log.info "Creating Device ID $id on Hub Id ${physicalHubs.size() > 1 ? installHub.id : physicalHubs[0].id}"
                def childDevice = addChildDevice("rboy", "Test Device", id.toString(), (physicalHubs.size() > 1 ? installHub.id : physicalHubs[0].id), [name: "Test Device $id", label: "Test Device $id", completedSetup: true])
            }

            existingDevices = getChildDevices()
        }

        log.trace "Working with devices $existingDevices"
        
        existingDevices.each { device ->
            def stuff = "DeviceID" + device.deviceNetworkId.toString()
            log.trace "Saving stuff to ${device.displayName}: $stuff"
            device.saveStuff(stuff)
            stuff = device.readStuff()
            log.debug "Read stuff from ${device.displayName}: $stuff"
            stuff = device.readStuffA()
            log.debug "Read stuff without return type from ${device.displayName}: $stuff"
        }
    } catch (e) {
        log.error "Error creating device: ${e}"
        throw e // Don't lose the exception here
    }
}
