package com.einvoive.helper;

import com.einvoive.model.LineItem;
import com.einvoive.model.Logs;
import com.einvoive.model.Settings;
import com.einvoive.repository.SettingsRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SettingsHelper {
    @Autowired
    SettingsRepository settingsRepository;
    @Autowired
    CompanyHelper companyHelper;
    @Autowired
    LogsHelper logsHelper;
    private Gson gson = new Gson();

    public String save(Settings settings) {
        try {
            settingsRepository.save(settings);
//            logsHelper.save(new Logs("Saving settings for "+companyHelper.getCompanyName(settings.get)))
            return "Settings saved";
        } catch (Exception ex) {
            return "Settings Not saved" + ex;
        }
    }

}
