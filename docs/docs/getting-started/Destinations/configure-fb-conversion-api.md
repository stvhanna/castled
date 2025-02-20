---
sidebar_position: 11
---

# Facebook Conversion Api

Facebook Conversion Api lets you sent Pixel events directly from your server. Server events are linked to a Meta Pixel ID and are processed like web events sent via Pixel. This means that web server events are used in measurement, reporting, and optimization in a similar way as browser Pixel events.

## Creating an app connection

![fb app config form](/img/screens/destinations/fb-conversion-api/app_config.png)

Follow the steps to get the **Pixel ID** and **Access Token** for your conversion api.

1. Login to you Facebook account
2. Go to `Events Manager`

   ![fb events manager](/img/screens/destinations/fb-conversion-api/fb_events_manager.png)

3. Under `Data Sources` select the pixel ID you want to use.
4. Click on the settings tab. You can find the **Pixel ID** here.

   ![fb events manager](/img/screens/destinations/fb-conversion-api/fb_pixel_id.png)

5. To fetch the **Access Token**, scroll down and click on `Generate access token`.

   ![fb events manager](/img/screens/destinations/fb-conversion-api/fb_access_token.png)

## Creating a sync pipeline

### Sync settings

![fb sync config form](/img/screens/destinations/fb-conversion-api/sync_config.png)

- **Action Source** - Allows you to specify where your conversions occurred. This field is optional if you are going to provide a mapping for `action_source` field in the **Server Event Parameters** in the mapping page. In case you specify both, latter will assume precedence.
- **Test Event Code** - In case you want to verify your events are delivered correctly to your pixel. To get the code navigate to `Test events` tab of your pixel.

![fb test event code](/img/screens/destinations/fb-conversion-api/fb_test_event_code.png)

- **Hashing Required** - Facebook requires all the personally identifiable user information to be hashed before syncing it. You can select **Yes** option if you want Castled to do the normalization and hashing. In case you already have the hashed information in your warehouse select **No**.

More information about facebook customer information parameters and hashing requirements can be found [here](https://developers.facebook.com/docs/marketing-api/conversions-api/parameters/customer-information-parameters)

### Event Field Mapping

Facebook conversion api events fields are classified into 4 groups in the mapping page.

1. **Server Event Parameters**

   `event_name` and `event_time` are mandatory. `action_source` is also a required field, you can skip it if provided in the sync settings section.

   ![fb server mapping](/img/screens/destinations/fb-conversion-api/fb_server_mapping.png)

2. **Customer Information Parameters**

   The customer information parameters are a set of user identifiers you share alongside your event information. Facebook use this for customer matching purposes only. Castled hashes this info before sending to Facebook.
   You **must** provide a mapping for at least one of this parameter when creating the pipeline.

   ![fb cutomer mapping](/img/screens/destinations/fb-conversion-api/fb_customer_mapping.png)

3. **Custom Data Parameters**

   Use these parameters to send additional data for ads delivery optimization. There are a bunch of predefined custom data parameters.

   ![fb custom data mapping](/img/screens/destinations/fb-conversion-api/fb_custom_data_mapping.png)

4. **User Defined Custom Properties**

   If you want to send any parameters in addition to the custom data parameters, you can use this section. A new custom property will be created corresponding to the name you provide.

   ![fb custom property](/img/screens/destinations/fb-conversion-api/fb_custom_property_mapping.png)

More details about these fields and their types can be found in the [Facebook conversion api paramaters doc](https://developers.facebook.com/docs/marketing-api/conversions-api/parameters).
