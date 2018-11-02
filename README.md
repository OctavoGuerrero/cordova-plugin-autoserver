# cordova-plugin-autoserver
This Apache Cordova plugin will serve your 'www' directory through a local web server.
This avoid cross-origin policy file:// protocol.

## How to use autoserver?

Add the plugin to your cordova project:

    cordova plugin add https://github.com/OctavoGuerrero/cordova-plugin-autoserver.git

After the plugin is installed, it changes the `config.xml` in your project root by modifying this tag to:

        <content src="http://localhost:8888" />

You don't need to do any more.

## Credits

The local web server implementation is from [NanoHTTPD](https://github.com/NanoHttpd/nanohttpd).

This Cordova plugin is built based on following 2 plugins, and thanks to the authors.<ul>
*    <li> [CorHttpd](https://github.com/floatinghotpot/cordova-httpd.git), a embeded httpd for cordova
*    <li> Scripts (after_install and before uninstall) are from [cordova-local-webserve](https://github.com/oakwood/cordova-local-webserver.git), a similar plugin, but only for ios.
* </ul>
