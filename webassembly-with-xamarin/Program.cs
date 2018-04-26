using System;
using Ooui;
using Xamarin.Forms;

namespace webassembly_with_xamarin
{
    class Program
    {
        static void Main(string[] args)
        {
            // Initialize Xamarin.Forms
            Forms.Init();

            // Create the UI
            var page = new ContentPage();
            var stack = new StackLayout();
            var button = new Xamarin.Forms.Button
            {
                Text = "Click me!"
            };
            stack.Children.Add(button);
            page.Content = stack;

            // Add some logic to it
            var count = 0;
            button.Clicked += (s, e) =>
            {
                count++;
                button.Text = $"Clicked {count} times";
            };

            // Publish a root element to be displayed
            UI.Publish("/", page.GetOouiElement());
        }
    }
}
