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
    section {
        input "devices", "capability.sensor", multiple: true
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
  devices.poll()
  runEvery5Minutes(devpoll) 
}

def devpoll() {
 devices.poll()
}