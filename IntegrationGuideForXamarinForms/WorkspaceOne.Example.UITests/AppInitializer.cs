using System;
using System.IO;
using System.Linq;
using Xamarin.UITest;
using Xamarin.UITest.Configuration;
using Xamarin.UITest.Queries;

namespace WorkspaceOne.Example.UITests
{
    public class AppInitializer
    {
        public static IApp StartApp(Platform platform)
        {
            if (platform == Platform.iOS)
            {
                return ConfigureApp
                    .iOS
                    .StartApp(AppDataMode.Clear);
            }

            return ConfigureApp
                .Android
                .StartApp(AppDataMode.Clear);
        }
    }
}
