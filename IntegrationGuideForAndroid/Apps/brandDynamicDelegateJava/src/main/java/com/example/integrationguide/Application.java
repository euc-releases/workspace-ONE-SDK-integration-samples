// Copyright 2023 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

package com.example.integrationguide;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;

import com.airwatch.app.AWSDKApplication;
import com.airwatch.app.AWSDKApplicationDelegate;
import com.airwatch.bizlib.beacon.BeaconEntity;
import com.airwatch.bizlib.callback.BeaconCallback;
import com.airwatch.bizlib.command.CommandSendThread;
import com.airwatch.bizlib.command.ICommandDefinitionFactory;
import com.airwatch.bizlib.command.chain.CommandProcessor;
import com.airwatch.certpinning.repository.CertPinRepository;
import com.airwatch.clipboard.CopyPasteManager;
import com.airwatch.crypto.MasterKeyManager;
import com.airwatch.event.WS1AnchorEvents;
import com.airwatch.keymanagement.unifiedpin.escrow.EscrowKeyManger;
import com.airwatch.keymanagement.unifiedpin.interfaces.TokenChangeChannel;
import com.airwatch.keymanagement.unifiedpin.interfaces.TokenChannel;
import com.airwatch.keymanagement.unifiedpin.interfaces.UnifiedPinInputManager;
import com.airwatch.keymanagement.unifiedpin.token.TokenFactory;
import com.airwatch.keymanagement.unifiedpin.token.TokenStorage;
import com.airwatch.login.branding.BrandingManager;
import com.airwatch.net.HttpServerConnection;
import com.airwatch.sdk.configuration.AppSettingFlags;
import com.airwatch.sdk.configuration.SettingsExtension;
import com.airwatch.sdk.context.awsdkcontext.SDKContextHelper;
import com.airwatch.sdk.context.awsdkcontext.handlers.SDKBaseHandler;
import com.airwatch.sdk.p2p.P2PChannel;
import com.vmware.chameleon.Configuration;
import com.vmware.chameleon.function.FunctionFactory;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;

public class Application
        extends android.app.Application
        implements AWSDKApplication
{

    // SDK Delegate.
    private final AWSDKApplicationDelegate awDelegate = new AWSDKApplicationDelegate();
    @NotNull
    @Override
    public AWSDKApplication getDelegate() {
        return awDelegate;
    }

    // Android Application overrides for integration.
    @Override
    public void onCreate() {
        super.onCreate();
        this.onCreate(this);
    }

    @Override
    public Object getSystemService(String name) {
        return this.getAWSystemService(name, super.getSystemService(name));
    }

    @Override
    public void attachBaseContext(@NotNull Context base) {
        super.attachBaseContext(base);
        attachBaseContext(this);
    }

    // Application-specific overrides.
    @Override
    public void onPostCreate() {
        // Code from the application's original onCreate() would go here.
    }

    @NonNull
    @Override
    public Intent getMainActivityIntent() {
        // Replace MainActivity with application's original main activity.
        return new Intent(getApplicationContext(), MainActivity.class);
    }

    // Mechanistic AWSDKApplication abstract method overrides.
    @Override
    public void attachBaseContext(@NotNull android.app.Application application) {
        awDelegate.attachBaseContext(application);
    }

    @Nullable
    @Override
    public Object getAWSystemService(@NotNull String name, @Nullable Object systemService) {
        return awDelegate.getAWSystemService(name, systemService);
    }

    @NonNull
    @Override
    public BeaconEntity getBeaconEntity(@androidx.annotation.Nullable Map<String, String> tokens) {
        return awDelegate.getBeaconEntity(tokens);
    }

    @NonNull
    @Override
    public BeaconCallback getBeaconCallback() {
        return awDelegate.getBeaconCallback();
    }

    @NonNull
    @Override
    public CommandProcessor getCommandProcessor() {
        return awDelegate.getCommandProcessor();
    }

    @NonNull
    @Override
    public ICommandDefinitionFactory getCommandDefinitionFactory() {
        return awDelegate.getCommandDefinitionFactory();
    }

    @NonNull
    @Override
    public CommandSendThread getCommandSendThread() {
        return awDelegate.getCommandSendThread();
    }

    @NonNull
    @Override
    public String getRequestorId() {
        return awDelegate.getRequestorId();
    }

    @androidx.annotation.Nullable
    @Override
    public HttpServerConnection getDeviceServiceUri() {
        return awDelegate.getDeviceServiceUri();
    }

    @NonNull
    @Override
    public byte[] getAppHmac() {
        return awDelegate.getAppHmac();
    }

    @NonNull
    @Override
    public CertPinRepository getRepository() {
        return awDelegate.getRepository();
    }

    @Override
    public void onCreate(@NotNull android.app.Application application) {
        awDelegate.onCreate(application);
    }

    @Override
    public void onSSLPinningValidationFailure(
            @NonNull String host,
            @androidx.annotation.Nullable X509Certificate serverCACertificate
    ) {
    }

    @Override
    public void onSSLPinningRequestFailure(
            @NonNull String host,
            @androidx.annotation.Nullable X509Certificate serverCACertificate
    ) {
    }

    @NotNull
    @Override
    public CopyPasteManager getCopyPasteManager() {
        return awDelegate.getCopyPasteManager();
    }

    @NonNull
    @Override
    public TokenChannel getTokenChannel() {
        return awDelegate.getTokenChannel();
    }

    @NonNull
    @Override
    public TokenChangeChannel getUnifiedPinChangePinManager() {
        return awDelegate.getUnifiedPinChangePinManager();
    }

    @NonNull
    @Override
    public TokenStorage getTokenStorage() {
        return awDelegate.getTokenStorage();
    }

    @NonNull
    @Override
    public TokenFactory getTokenFactory() {
        return awDelegate.getTokenFactory();
    }

    @NonNull
    @Override
    public EscrowKeyManger getEscrowKeyManager() {
        return awDelegate.getEscrowKeyManager();
    }

    @NonNull
    @Override
    public UnifiedPinInputManager getUnifiedPinInputManager() {
        return awDelegate.getUnifiedPinInputManager();
    }

    @NonNull
    @Override
    public Intent getNotificationActivityIntent() {
        return awDelegate.getNotificationActivityIntent();
    }

    @NonNull
    @Override
    public BrandingManager getBrandingManager() {
        return awDelegate.getBrandingManager();
    }

    @Override
    public boolean isInputLogoBrandable() {
        return awDelegate.isInputLogoBrandable();
    }

    @Override
    public void onTLSCertificateRotationRequired(
            @NonNull String alias,
            int certifcateState,
            long certificateExpiryTime
    ) {
        awDelegate.onTLSCertificateRotationRequired(alias, certifcateState, certificateExpiryTime);
    }

    @Override
    public void onSecureMessageReceived(@NotNull String message) {
        awDelegate.onSecureMessageReceived(message);
    }

    @Override
    public int getScheduleSdkFetchTime() {
        return awDelegate.getScheduleSdkFetchTime();
    }

    @Override
    public int getSettingsFetchThresholdTimeInMillis() {
        return awDelegate.getSettingsFetchThresholdTimeInMillis();
    }

    @Override
    public int getFeatureConfigFetchTimeMillis() {
        return awDelegate.getFeatureConfigFetchTimeMillis();
    }

    @NonNull
    @Override
    public List<SDKBaseHandler> getAppExtraSteps(SDKContextHelper.AWContextCallBack chainCallBack) {
        return awDelegate.getAppExtraSteps(chainCallBack);
    }

    @Override
    public boolean requiresSDKSettings() {
        return awDelegate.requiresSDKSettings();
    }

    @androidx.annotation.Nullable
    @Override
    public String getApplicationConfigType() {
        return awDelegate.getApplicationConfigType();
    }

    @androidx.annotation.Nullable
    @Override
    public String getApplicationConfigVersion() {
        return awDelegate.getApplicationConfigVersion();
    }

    @NonNull
    @Override
    public String getSDKConfigType() {
        return awDelegate.getSDKConfigType();
    }

    @NonNull
    @Override
    public SettingsExtension getSettingsExtension() {
        return awDelegate.getSettingsExtension();
    }

    @Override
    public boolean isAppProcess() {
        return awDelegate.isAppProcess();
    }

    @Override
    public boolean isGeofencingSupported() {
        return awDelegate.isGeofencingSupported();
    }

    @NonNull
    @Override
    public AppSettingFlags getFlags() {
        return awDelegate.getFlags();
    }

    @Override
    public void onConfigurationChanged(@androidx.annotation.Nullable Set<String> keySet) {
        awDelegate.onConfigurationChanged(keySet);
    }

    @Override
    public boolean getMagCertificateEnable() {
        return awDelegate.getMagCertificateEnable();
    }

    @androidx.annotation.Nullable
    @Override
    public Intent getMainLauncherIntent() {
        return awDelegate.getMainLauncherIntent();
    }

    @NonNull
    @Override
    public String getAppTextEula() {
        return awDelegate.getAppTextEula();
    }

    @Override
    public int getNotificationIcon() {
        return awDelegate.getNotificationIcon();
    }

    @Override
    public int getNightMode() {
        return AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
    }

    @NonNull
    @Override
    public Context getAwAppContext() {
        return awDelegate.getAwAppContext();
    }

    @NonNull
    @Override
    public byte[] getPassword() {
        return awDelegate.getPassword();
    }

    @androidx.annotation.Nullable
    @Override
    public MasterKeyManager getKeyManager() {
        return awDelegate.getKeyManager();
    }

    @Override
    public boolean getIsStandAloneAllowed() {
        return awDelegate.getIsStandAloneAllowed();
    }

    @androidx.annotation.Nullable
    @Override
    public P2PChannel getChannel(@NonNull String channelIdentifier) {
        return awDelegate.getChannel(channelIdentifier);
    }

    @Override
    public void registerChannel(@NonNull String channelIdentifier, P2PChannel channel) {
        awDelegate.registerChannel(channelIdentifier, channel);
    }

    @Override
    public void unRegisterChannel(@NonNull String channelIdentifier) {
        awDelegate.unRegisterChannel(channelIdentifier);
    }

    @Override
    public void initService(String channelIdentifier) {
        awDelegate.initService(channelIdentifier);
    }

    @Override
    public boolean shouldRegisterForSSO() {
        return awDelegate.shouldRegisterForSSO();
    }

    @Override
    public void onPreferenceError(
            @NonNull PreferenceErrorCode code,
            String... errorMessage
    ) {
        awDelegate.onPreferenceError(code, errorMessage);
    }

    @Override
    public void onSDKException(
            @NonNull PreferenceErrorCode code,
            String... errorMessage
    ) {
        awDelegate.onSDKException(code, errorMessage);
    }

    @Override
    public void initializeLogger() {
        awDelegate.initializeLogger();
    }

    @Override
    public void chameleonContextCreate(@NotNull Context context) {
        awDelegate.chameleonContextCreate(context);
    }

    @NotNull
    @Override
    public Future<Boolean> emitEvent(@NotNull String s1, @NotNull String s2, boolean b) {
        return awDelegate.emitEvent(s1, s2, b);
    }

    @NotNull
    @Override
    public Future<Boolean> evaluateScript(@NotNull String s) {
        return awDelegate.evaluateScript(s);
    }

    @NotNull
    @Override
    public Future<Boolean> hasEvent(@NotNull String s) {
        return awDelegate.hasEvent(s);
    }

    @Override
    public void registerFunction(@NotNull String s, @NotNull FunctionFactory functionFactory) {
        awDelegate.registerFunction(s, functionFactory);
    }

    @NotNull
    @Override
    public Future<Boolean> setResourcePathMapping(@NotNull String s, @NotNull String s1) {
        return  awDelegate.setResourcePathMapping(s, s1);
    }

    @NotNull
    @Override
    public Future<Boolean> setStoragePathMapping(@NotNull String s, @NotNull String s1) {
        return awDelegate.setStoragePathMapping(s, s1);
    }

    @NotNull
    @Override
    public Future<Boolean> startChameleonContext(@NotNull Configuration configuration) {
        return awDelegate.startChameleonContext(configuration);
    }

    @Override
    public void stopChameleonContext() {
        awDelegate.stopChameleonContext();
    }

    @Override
    public void initializeFeatureModule() {
        awDelegate.initializeFeatureModule();
    }

    @Override
    public void initialiseOperationalData() {
        awDelegate.initialiseOperationalData();
    }

    @NonNull
    @Override
    public WS1AnchorEvents getEventHandler() {
        return new AppWS1AnchorEvents();
    }

    @NonNull
    @Override
    public Future<Boolean> emitEvent(@NonNull String s, boolean b) {
        return awDelegate.emitEvent(s, b);
    }

    @NonNull
    @Override
    public Future<Boolean> emitEvent(@NonNull String s, @NonNull byte[] bytes) {
        return awDelegate.emitEvent(s, bytes);
    }

    @NonNull
    @Override
    public Future<Boolean> emitEvent(@NonNull String s, @NonNull Number number) {
        return awDelegate.emitEvent(s, number);
    }

    @Override
    public boolean loadModule(@NonNull String s) {
        return awDelegate.loadModule(s);
    }

    @Override
    public void registerAsyncFunction(@NonNull String s, @NonNull FunctionFactory functionFactory) {
        awDelegate.registerAsyncFunction(s, functionFactory);
    }
}