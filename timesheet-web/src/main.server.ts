import { BootstrapContext, bootstrapApplication } from '@angular/platform-browser';
import { provideHttpClient } from '@angular/common/http';  // ✅ Import this
import { App } from './app/app';
import { config } from './app/app.config.server';

const bootstrap = (context: BootstrapContext) =>
  bootstrapApplication(App, {
    ...config,
    providers: [
      provideHttpClient(),      // ✅ Provide HttpClient for SSR
      ...(config.providers || [])
    ]
  }, context);

export default bootstrap;
