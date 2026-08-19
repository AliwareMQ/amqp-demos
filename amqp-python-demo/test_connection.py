import ssl
import unittest
from unittest import mock

import connection


class ConnectionParametersTest(unittest.TestCase):

    def test_tls_connection_uses_verified_system_ca_context(self):
        self.assertTrue(connection.verifyServerCertificate)

        with mock.patch.object(connection, "port", 5671):
            parameters = connection.get_connection_param()

        ssl_options = parameters.ssl_options
        context = ssl_options.context

        self.assertTrue(context.check_hostname)
        self.assertEqual(context.verify_mode, ssl.CERT_REQUIRED)
        self.assertGreater(context.cert_store_stats()["x509_ca"], 0)
        self.assertEqual(ssl_options.server_hostname, connection.host)

    def test_tls_connection_can_disable_certificate_verification_explicitly(self):
        with mock.patch.object(connection, "port", 5671), \
                mock.patch.object(connection, "verifyServerCertificate", False):
            parameters = connection.get_connection_param()

        ssl_options = parameters.ssl_options
        context = ssl_options.context

        self.assertFalse(context.check_hostname)
        self.assertEqual(context.verify_mode, ssl.CERT_NONE)
        self.assertEqual(ssl_options.server_hostname, connection.host)


if __name__ == "__main__":
    unittest.main()
