import calendar from './calendar.svg'
import customerSupport from './customer-support.svg'
import divider from './divider.svg'
import exchange from './exchange.svg'
import signUp from './sign-up.svg'
import translate from './translate.svg'
import user from './user.svg'

export type IconAsset = {
  src: string
  /** Intrinsic size of the exported SVG (its root width/height attributes). */
  svgWidth: number
  svgHeight: number
  /** Layout box the icon occupies in the Figma design. */
  boxWidth: number
  boxHeight: number
}

// SVGs exported from the Figma Home frame; box sizes are the layer sizes in the design.
export const icons = {
  user: { src: user, svgWidth: 20, svgHeight: 20, boxWidth: 19.375, boxHeight: 20 },
  signUp: { src: signUp, svgWidth: 16, svgHeight: 18, boxWidth: 15.079, boxHeight: 18 },
  translate: { src: translate, svgWidth: 18, svgHeight: 18, boxWidth: 18, boxHeight: 18 },
  customerSupport: { src: customerSupport, svgWidth: 21, svgHeight: 19, boxWidth: 20.703, boxHeight: 19 },
  exchange: { src: exchange, svgWidth: 24, svgHeight: 24, boxWidth: 24, boxHeight: 24 },
  calendar: { src: calendar, svgWidth: 19, svgHeight: 20, boxWidth: 18.868, boxHeight: 20 },
  divider: { src: divider, svgWidth: 1, svgHeight: 9, boxWidth: 0, boxHeight: 9 },
} satisfies Record<string, IconAsset>

export type IconName = keyof typeof icons
