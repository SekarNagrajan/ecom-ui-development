import { icons, type IconName } from '../assets/icons'

type IconProps = {
  name: IconName
  className?: string
}

/**
 * Renders a design SVG at its intrinsic size inside a box matching the Figma layer,
 * so surrounding flex gaps line up with the design.
 */
export default function Icon({ name, className = '' }: IconProps) {
  const icon = icons[name]
  return (
    <span
      aria-hidden="true"
      className={`relative block shrink-0 ${className}`}
      style={{ width: icon.boxWidth, height: icon.boxHeight }}
    >
      <img
        src={icon.src}
        width={icon.svgWidth}
        height={icon.svgHeight}
        alt=""
        className="absolute top-0 left-0 max-w-none"
      />
    </span>
  )
}
