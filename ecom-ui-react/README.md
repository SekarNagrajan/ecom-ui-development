# ecom-ui-react

React + Vite + TypeScript + Tailwind CSS implementation of the e-commerce **Home** page from the
Figma file `19082026_ecom_design_v1` (frame "Home", 1920 × 1621).

## Commands

```bash
npm install
npm run dev      # http://localhost:5173
npm run build    # type-check + production build into dist/
npm run preview  # serve the production build
npm run lint
```

## Structure

```
src/
  assets/icons/      SVGs exported from the Figma layers + size registry (index.ts)
  assets/images/     Hero photo and Solverminds logo exported from Figma
  components/        Reusable UI: ActionButton, Icon, FieldShell, LocationField, DateField
  sections/          Page sections: SiteHeader, Hero, ScheduleSearchCard, HowItWorks, SignupBanner, SiteFooter
  pages/HomePage.tsx Page composition
  config/links.ts    Link destinations (placeholders until real routes exist)
  index.css          Tailwind setup, design tokens (@theme) and page-width shells
```

Fonts are self-hosted via `@fontsource/poppins` (300, 400, 500, 600, 800).

## Behaviour

- Search tabs (Schedules / Tracking / Rates) switch the active tab.
- The swap button exchanges Origin and Destination.
- Date fields open the native date picker; "To Date" can't be earlier than "From Date".
- The search form calls the optional `onSearch` prop of `ScheduleSearchCard` with the query.
- Login / Register / Contact Us and footer links point at placeholders in `src/config/links.ts`.
