##General
This Java program is intended to "fix" Speedport W724V Typ A's misrouting of packets to destinations in the 169.0.0.0 subnet.
Due to wrong settings by the manufacturer, any packet gets routed to the router itself, and thus does not reach its intended endpoint.
A common symptom of that behaviour is that WhatsApp media files (images, audio, ...) can not be sent nor received over WiFi.
To "fix" that, this program logs into the router and deletes the incorrect route.
This however only persists until the next reboot of your router. Then you have to run this program again.

###Usage
Just start the jar file.

###Used libraries

* [Google GSON](https://github.com/google/gson)
* [Apache HttpClient](https://hc.apache.org/httpcomponents-client-ga/index.html) 

##Disclaimer
Please make sure to backup your current settings (through the webinterface).
I am not affiliated to either Deutsche Telekom nor WhatsApp, all trademarks belong to them.
Please note that this is not official at all and can damage your router.
In case something goes wrong, just restart your router and everything should be fine again.

##License
My program is licensed under GPLv3:
Copyright (C) 2016 Marco Ammon <ammon.marco@t-online.de>
This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 3
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
