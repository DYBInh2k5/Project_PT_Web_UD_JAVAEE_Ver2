# Design System: Gótico Moderno

## 1. Definição do Estilo

- **Nome:** Gótico Moderno
- **Tipo:** Dark, Elegant, Edgy
- **Keywords:** modern gothic, dark, elegant, edgy, romantic, intricate, high-contrast, moody, sophisticated, architectural
- **Era:** Contemporary Gothic Revival
- **Light/Dark:** ✗ No / ✓ Full

## 2. Paleta de Cores

- **Primárias:** Onyx Black #1A1A1A, Blood Red #8B0000, Silver #C0C0C0, Off-White #F5F5F5
- **Secundárias:** Deep Purple #480048, Forest Green #228B22, White #FFFFFF

## 3. Efeitos Visuais

High-contrast typography, intricate line art, modern blackletter fonts, dramatic photography, subtle smoke effects, sharp architectural lines, romantic floral elements, elegant animations

## 4. AI Prompt Keywords

Design a modern gothic landing page. Use: onyx black and blood red, high-contrast typography, intricate line art, modern blackletter fonts, dramatic photography, subtle smoke effects, sharp architectural lines, romantic floral elements.

## 5. CSS Technical

```css
background: #1A1A1A, color: #F5F5F5, font-family: 'UnifrakturMaguntia', cursive, border: 1px solid #C0C0C0, box-shadow: 0 0 15px rgba(192,192,192,0.3), text-shadow: 0 0 5px #8B0000, animation: fade-in-out 4s infinite alternate, filter: contrast(1.2), background-image: url('gothic-pattern.svg'), background-blend-mode: overlay
```

## 6. Design System Variables

```css
--onyx-black-gothic: #1A1A1A; /* primary background, dark surfaces */
--blood-red-gothic: #8B0000; /* error states, destructive actions, hero text */
--silver-gothic: #C0C0C0; /* supporting palette, borders, muted text */
--off-white-gothic: #F5F5F5; /* light surfaces, card text, body text */
--deep-purple-gothic: #480048; /* accent color, emphasis elements */
--forest-green-gothic: #228B22; /* success states, positive indicators */
--white-gothic: #FFFFFF; /* secondary surface, high-contrast text */
--line-art-thickness: 1px;
--font-gothic: 'UnifrakturMaguntia', cursive;
--font-body: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif;
--font-mono: 'JetBrains Mono', monospace;
```

## 7. Checklist de Implementação

- [x] High-contrast typography
- [x] Intricate line art (decorative borders)
- [x] Modern blackletter fonts (UnifrakturMaguntia for hero/headings)
- [x] Dramatic photography (high-contrast product images)
- [x] Subtle smoke/fog effects (CSS gradients/blur overlays)
- [x] Sharp architectural lines (clear grid layout, bordered sections)

## 8. Visual Theme & Atmosphere

Gótico Moderno — Design general com modern gothic, dark, elegant. Estilo Gótico Moderno representa uma tendência moderna em design UI/UX web com foco em general.

- Density: 5/10 — Balanced
- Variance: 4/10 — Moderate
- Motion: 6/10 — Expressive

## 9. Color Palette & Roles

- **Onyx Black** (#1A1A1A) — Dark surface, primary background
- **Blood Red** (#8B0000) — Hero emphasis, accent elements, hover states
- **Silver** (#C0C0C0) — Borders, decorative lines, secondary text
- **Off-White** (#F5F5F5) — Body text, primary content text
- **Deep Purple** (#480048) — Footer background, section accents
- **Forest Green** (#228B22) — Success states, stock availability
- **White** (#FFFFFF) — High-emphasis text, headings

## 10. Typography Rules

- **Display / Hero:** UnifrakturMaguntia — Weight 700, tight tracking, used for headline impact
- **Body:** Inter — Weight 400, 16px/1.6 line-height, max 72ch per line
- **UI Labels / Captions:** Inter — 0.875rem, weight 500, slight letter-spacing
- **Monospace:** JetBrains Mono — Used for code, metadata, and technical values

Scale:
- Hero: clamp(2.5rem, 5vw, 4rem)
- H1: 2.25rem
- H2: 1.5rem
- Body: 1rem / 1.6
- Small: 0.875rem

## 11. Component Stylings

- **Primary Button:** Blood red fill (#8B0000), subtly rounded (0.5rem). Hover: darken + lift shadow. Active: translate press. Font weight 600.
- **Secondary / Ghost Button:** Outline variant. 1.5px border silver. Text off-white. Hover: subtle background fill.
- **Cards:** Onyx black surface. 1px silver border stroke. Subtle shadow. Rounded corners (0.5rem).
- **Inputs:** Dark surface. 1px silver border. Focus ring: 2px blood red. Off-white text.
- **Navigation:** Onyx black background. Active item: blood red underline. Font weight 500.
- **Tables:** Dark rows alternating. Silver borders. Off-white text.
- **Empty States:** Icon-based composition with descriptive text and action button.

## 12. Layout Principles

- **Grid:** CSS Grid primary. Max-width containment: 1280px centered with 1.5rem side padding.
- **Spacing rhythm:** Balanced. Base unit: 0.5rem (8px).
- **Section vertical gaps:** clamp(4rem, 8vw, 8rem).
- **Hero layout:** Full-width dark hero with centered text and decorative borders.
- **Mobile collapse:** All multi-column layouts collapse below 768px. No horizontal overflow.
- **z-index contract:** base (0) / sticky-nav (100) / overlay (200) / modal (300) / toast (500).

## 13. Motion & Interaction

- **Transitions:** All interactive elements have 200ms ease transitions.
- **Hover states:** Scale(1.02) + shadow lift over 200ms for cards. Color transitions for buttons/links.
- **Entry animations:** Fade + translate-Y (16px → 0) over 480ms ease-out.
- **Performance:** Only transform and opacity animated. No layout-triggering properties.

## 14. Anti-Patterns (Banned)

- No emojis in UI — use icon system only (Font Awesome)
- No pure white (#FFFFFF) backgrounds — use off-white or dark surfaces
- No oversaturated accent colors (saturation cap: 80%)
- No 3-column equal-width feature layouts
- No `h-screen` — use `min-h-[100dvh]`
- No broken external image links
- No generic lorem ipsum in demos

## Contexto Histórico

Estilo Gótico Moderno representa uma tendência moderna em design UI/UX web com foco em general.

## Caso de Uso

Landing pages, SaaS, E-commerce
